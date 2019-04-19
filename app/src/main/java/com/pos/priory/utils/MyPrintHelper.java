package com.pos.priory.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyPrintHelper {
    private static final String LOG_TAG = "MyPrintHelper";
    private static final int MAX_PRINT_SIZE = 3500;
    static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
    static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
    public static final int SCALE_MODE_FIT = 1;
    public static final int SCALE_MODE_FILL = 2;
    @SuppressLint({"InlinedApi"})
    public static final int COLOR_MODE_MONOCHROME = 1;
    @SuppressLint({"InlinedApi"})
    public static final int COLOR_MODE_COLOR = 2;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    final Context mContext;
    BitmapFactory.Options mDecodeOptions = null;
    final Object mLock = new Object();
    int mScaleMode = 2;
    int mColorMode = 2;
    int mOrientation = 1;

    public static boolean systemSupportsPrint() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public MyPrintHelper(@NonNull Context context) {
        this.mContext = context;
    }

    public void setScaleMode(int scaleMode) {
        this.mScaleMode = scaleMode;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public void setColorMode(int colorMode) {
        this.mColorMode = colorMode;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public int getOrientation() {
        return Build.VERSION.SDK_INT >= 19 && this.mOrientation == 0 ? 1 : this.mOrientation;
    }

    public void printBitmap(@NonNull String jobName, @NonNull List<Bitmap> bitmaps) {
        this.printBitmap(jobName, bitmaps, (MyPrintHelper.OnPrintFinishCallback) null);
    }

    public void printBitmap(@NonNull String jobName, @NonNull List<Bitmap> bitmaps, @Nullable MyPrintHelper.OnPrintFinishCallback callback) {
        if (Build.VERSION.SDK_INT >= 19 && bitmaps != null) {
            PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
            PrintAttributes.MediaSize mediaSize;
            if (isPortrait(bitmaps.get(0))) {
                mediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
            } else {
                mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
            }

            PrintAttributes attr = (new PrintAttributes.Builder()).setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
            printManager.print(jobName, new MyPrintHelper.PrintBitmapAdapter(jobName, this.mScaleMode, bitmaps, callback), attr);
        }
    }

    public void printBitmap(@NonNull String jobName, @NonNull Uri imageFile) throws FileNotFoundException {
        this.printBitmap(jobName, (Uri) imageFile, (MyPrintHelper.OnPrintFinishCallback) null);
    }

    public void printBitmap(@NonNull String jobName, @NonNull Uri imageFile, @Nullable MyPrintHelper.OnPrintFinishCallback callback) throws FileNotFoundException {
        if (Build.VERSION.SDK_INT >= 19) {
            PrintDocumentAdapter printDocumentAdapter = new MyPrintHelper.PrintUriAdapter(jobName, imageFile, callback, this.mScaleMode);
            PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setColorMode(this.mColorMode);
            if (this.mOrientation != 1 && this.mOrientation != 0) {
                if (this.mOrientation == 2) {
                    builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
                }
            } else {
                builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
            }

            PrintAttributes attr = builder.build();
            printManager.print(jobName, printDocumentAdapter, attr);
        }
    }

    static boolean isPortrait(Bitmap bitmap) {
        return bitmap.getWidth() <= bitmap.getHeight();
    }

    @RequiresApi(19)
    private static PrintAttributes.Builder copyAttributes(PrintAttributes other) {
        PrintAttributes.Builder b = (new PrintAttributes.Builder()).setMediaSize(other.getMediaSize()).setResolution(other.getResolution()).setMinMargins(other.getMinMargins());
        if (other.getColorMode() != 0) {
            b.setColorMode(other.getColorMode());
        }

        if (Build.VERSION.SDK_INT >= 23 && other.getDuplexMode() != 0) {
            b.setDuplexMode(other.getDuplexMode());
        }

        return b;
    }

    static Matrix getMatrix(int imageWidth, int imageHeight, RectF content, int fittingMode) {
        Matrix matrix = new Matrix();
        float scale = content.width() / (float) imageWidth;
        if (fittingMode == 2) {
            scale = Math.max(scale, content.height() / (float) imageHeight);
        } else {
            scale = Math.min(scale, content.height() / (float) imageHeight);
        }

        matrix.postScale(scale, scale);
        float translateX = (content.width() - (float) imageWidth * scale) / 2.0F;
        float translateY = (content.height() - (float) imageHeight * scale) / 2.0F;
        matrix.postTranslate(translateX, translateY);
        return matrix;
    }

    int pos = 0;

    @RequiresApi(19)
    void writeBitmaps(final PrintAttributes attributes, final int fittingMode, final List<Bitmap> bitmaps, final ParcelFileDescriptor fileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
        final PrintAttributes pdfAttributes;
        if (IS_MIN_MARGINS_HANDLING_CORRECT) {
            pdfAttributes = attributes;
        } else {
            pdfAttributes = copyAttributes(attributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
        }
        pos = 0;
        for (int i = 0; i < bitmaps.size(); i++) {
            final Bitmap bitmap = bitmaps.get(i);
            final int index = i;
            new AsyncTask<Void, Void, Throwable>() {
                protected Throwable doInBackground(Void... params) {
                    try {
                        Log.e("MyPrintHelper", "doInBackground:" + index);
                        if (cancellationSignal.isCanceled()) {
                            Log.e("MyPrintHelper", "cancellationSignal.isCanceled():" + index);
                            return null;
                        } else {
                            PrintedPdfDocument pdfDocument = new PrintedPdfDocument(MyPrintHelper.this.mContext, pdfAttributes);
                            Bitmap maybeGrayscale = MyPrintHelper.convertBitmapForColorMode(bitmap, pdfAttributes.getColorMode());
                            if (cancellationSignal.isCanceled()) {
                                return null;
                            } else {
                                PdfDocument.Page dummyPage;
                                try {
                                    PdfDocument.Page page = pdfDocument.startPage(index);
                                    RectF contentRect;
                                    if (MyPrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                                        Log.e("MyPrintHelper", "MyPrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT");
                                        contentRect = new RectF(page.getInfo().getContentRect());
                                    } else {
                                        Log.e("MyPrintHelper", "not MyPrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT");
                                        PrintedPdfDocument dummyDocument = new PrintedPdfDocument(MyPrintHelper.this.mContext, attributes);
                                        dummyPage = dummyDocument.startPage(index);
                                        contentRect = new RectF(dummyPage.getInfo().getContentRect());
                                        dummyDocument.finishPage(dummyPage);
                                        dummyDocument.close();
                                    }

                                    Matrix matrix = MyPrintHelper.getMatrix(maybeGrayscale.getWidth(), maybeGrayscale.getHeight(), contentRect, fittingMode);
                                    if (!MyPrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                                        matrix.postTranslate(contentRect.left, contentRect.top);
                                        page.getCanvas().clipRect(contentRect);
                                    }

                                    page.getCanvas().drawBitmap(maybeGrayscale, matrix, (Paint) null);
                                    pdfDocument.finishPage(page);
                                    if (cancellationSignal.isCanceled()) {
                                        dummyPage = null;
                                        return null;
                                    }

                                    pdfDocument.writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
                                    Log.e("MyPrintHelper", "pdfDocument.writeTo:" + index);
                                    dummyPage = null;
                                } finally {
                                    pdfDocument.close();
                                    if (fileDescriptor != null) {
                                        try {
                                            fileDescriptor.close();
                                        } catch (IOException var16) {
                                            ;
                                        }
                                    }

                                    if (maybeGrayscale != bitmap) {
                                        maybeGrayscale.recycle();
                                    }

                                }

                                return null;
                            }
                        }
                    } catch (Throwable var18) {
                        return var18;
                    }
                }

                protected void onPostExecute(Throwable throwable) {
                    pos++;
                    if (pos == 3) {
                        Log.e("MyPrintHelper", "onPostExecute:" + pos);
                        if (cancellationSignal.isCanceled()) {
                            writeResultCallback.onWriteCancelled();
                        } else if (throwable == null) {
                            writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                        } else {
                            Log.e("MyPrintHelper", "Error writing printed content", throwable);
                            writeResultCallback.onWriteFailed((CharSequence) null);
                        }
                    }
                }
            }.execute(new Void[0]);
        }
    }

    @RequiresApi(19)
    void writeBitmap(final PrintAttributes attributes, final int fittingMode, final Bitmap bitmap, final ParcelFileDescriptor fileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
        final PrintAttributes pdfAttributes;
        if (IS_MIN_MARGINS_HANDLING_CORRECT) {
            pdfAttributes = attributes;
        } else {
            pdfAttributes = copyAttributes(attributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
        }

        (new AsyncTask<Void, Void, Throwable>() {
            protected Throwable doInBackground(Void... params) {
                try {
                    if (cancellationSignal.isCanceled()) {
                        return null;
                    } else {
                        PrintedPdfDocument pdfDocument = new PrintedPdfDocument(MyPrintHelper.this.mContext, pdfAttributes);
                        Bitmap maybeGrayscale = MyPrintHelper.convertBitmapForColorMode(bitmap, pdfAttributes.getColorMode());
                        if (cancellationSignal.isCanceled()) {
                            return null;
                        } else {
                            PdfDocument.Page dummyPage;
                            try {
                                PdfDocument.Page page = pdfDocument.startPage(1);
                                RectF contentRect;
                                if (MyPrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                                    contentRect = new RectF(page.getInfo().getContentRect());
                                } else {
                                    PrintedPdfDocument dummyDocument = new PrintedPdfDocument(MyPrintHelper.this.mContext, attributes);
                                    dummyPage = dummyDocument.startPage(1);
                                    contentRect = new RectF(dummyPage.getInfo().getContentRect());
                                    dummyDocument.finishPage(dummyPage);
                                    dummyDocument.close();
                                }

                                Matrix matrix = MyPrintHelper.getMatrix(maybeGrayscale.getWidth(), maybeGrayscale.getHeight(), contentRect, fittingMode);
                                if (!MyPrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                                    matrix.postTranslate(contentRect.left, contentRect.top);
                                    page.getCanvas().clipRect(contentRect);
                                }

                                page.getCanvas().drawBitmap(maybeGrayscale, matrix, (Paint) null);
                                pdfDocument.finishPage(page);
                                if (cancellationSignal.isCanceled()) {
                                    dummyPage = null;
                                    return null;
                                }

                                pdfDocument.writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
                                dummyPage = null;
                            } finally {
                                pdfDocument.close();
                                if (fileDescriptor != null) {
                                    try {
                                        fileDescriptor.close();
                                    } catch (IOException var16) {
                                        ;
                                    }
                                }

                                if (maybeGrayscale != bitmap) {
                                    maybeGrayscale.recycle();
                                }

                            }

                            return null;
                        }
                    }
                } catch (Throwable var18) {
                    return var18;
                }
            }

            protected void onPostExecute(Throwable throwable) {
                if (cancellationSignal.isCanceled()) {
                    writeResultCallback.onWriteCancelled();
                } else if (throwable == null) {
                    writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } else {
                    Log.e("MyPrintHelper", "Error writing printed content", throwable);
                    writeResultCallback.onWriteFailed((CharSequence) null);
                }

            }
        }).execute(new Void[0]);
    }

    Bitmap loadConstrainedBitmap(Uri uri) throws FileNotFoundException {
        if (uri != null && this.mContext != null) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            this.loadBitmap(uri, opt);
            int w = opt.outWidth;
            int h = opt.outHeight;
            if (w > 0 && h > 0) {
                int imageSide = Math.max(w, h);

                int sampleSize;
                for (sampleSize = 1; imageSide > 3500; sampleSize <<= 1) {
                    imageSide >>>= 1;
                }

                if (sampleSize > 0 && 0 < Math.min(w, h) / sampleSize) {
                    Object var8 = this.mLock;
                    BitmapFactory.Options decodeOptions;
                    synchronized (this.mLock) {
                        this.mDecodeOptions = new BitmapFactory.Options();
                        this.mDecodeOptions.inMutable = true;
                        this.mDecodeOptions.inSampleSize = sampleSize;
                        decodeOptions = this.mDecodeOptions;
                    }

                    boolean var18 = false;

                    Bitmap var23;
                    try {
                        var18 = true;
                        var23 = this.loadBitmap(uri, decodeOptions);
                        var18 = false;
                    } finally {
                        if (var18) {
                            Object var12 = this.mLock;
                            synchronized (this.mLock) {
                                this.mDecodeOptions = null;
                            }
                        }
                    }

                    Object var9 = this.mLock;
                    synchronized (this.mLock) {
                        this.mDecodeOptions = null;
                        return var23;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
    }

    private Bitmap loadBitmap(Uri uri, BitmapFactory.Options o) throws FileNotFoundException {
        if (uri != null && this.mContext != null) {
            InputStream is = null;

            Bitmap var4;
            try {
                is = this.mContext.getContentResolver().openInputStream(uri);
                var4 = BitmapFactory.decodeStream(is, (Rect) null, o);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException var11) {
                        Log.w("MyPrintHelper", "close fail ", var11);
                    }
                }

            }

            return var4;
        } else {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
    }

    static Bitmap convertBitmapForColorMode(Bitmap original, int colorMode) {
        if (colorMode != 1) {
            return original;
        } else {
            Bitmap grayscale = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(grayscale);
            Paint p = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0.0F);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            p.setColorFilter(f);
            c.drawBitmap(original, 0.0F, 0.0F, p);
            c.setBitmap((Bitmap) null);
            return grayscale;
        }
    }

    static {
        PRINT_ACTIVITY_RESPECTS_ORIENTATION = Build.VERSION.SDK_INT < 20 || Build.VERSION.SDK_INT > 23;
        IS_MIN_MARGINS_HANDLING_CORRECT = Build.VERSION.SDK_INT != 23;
    }

    @RequiresApi(19)
    private class PrintUriAdapter extends PrintDocumentAdapter {
        final String mJobName;
        final Uri mImageFile;
        final MyPrintHelper.OnPrintFinishCallback mCallback;
        final int mFittingMode;
        PrintAttributes mAttributes;
        AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
        Bitmap mBitmap;

        PrintUriAdapter(String jobName, Uri imageFile, MyPrintHelper.OnPrintFinishCallback callback, int fittingMode) {
            this.mJobName = jobName;
            this.mImageFile = imageFile;
            this.mCallback = callback;
            this.mFittingMode = fittingMode;
            this.mBitmap = null;
        }

        public void onLayout(final PrintAttributes oldPrintAttributes, final PrintAttributes newPrintAttributes, final CancellationSignal cancellationSignal, final LayoutResultCallback layoutResultCallback, Bundle bundle) {
            synchronized (this) {
                this.mAttributes = newPrintAttributes;
            }

            if (cancellationSignal.isCanceled()) {
                layoutResultCallback.onLayoutCancelled();
            } else if (this.mBitmap != null) {
                PrintDocumentInfo info = (new android.print.PrintDocumentInfo.Builder(this.mJobName)).setContentType(1).setPageCount(1).build();
                boolean changed = !newPrintAttributes.equals(oldPrintAttributes);
                layoutResultCallback.onLayoutFinished(info, changed);
            } else {
                this.mLoadBitmap = (new AsyncTask<Uri, Boolean, Bitmap>() {
                    protected void onPreExecute() {
                        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                            public void onCancel() {
                                MyPrintHelper.PrintUriAdapter.this.cancelLoad();
                                cancel(false);
                            }
                        });
                    }

                    protected Bitmap doInBackground(Uri... uris) {
                        try {
                            return MyPrintHelper.this.loadConstrainedBitmap(MyPrintHelper.PrintUriAdapter.this.mImageFile);
                        } catch (FileNotFoundException var3) {
                            return null;
                        }
                    }

                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        if (bitmap != null && (!MyPrintHelper.PRINT_ACTIVITY_RESPECTS_ORIENTATION || MyPrintHelper.this.mOrientation == 0)) {
                            PrintAttributes.MediaSize mediaSize;
                            synchronized (this) {
                                mediaSize = MyPrintHelper.PrintUriAdapter.this.mAttributes.getMediaSize();
                            }

                            if (mediaSize != null && mediaSize.isPortrait() != MyPrintHelper.isPortrait(bitmap)) {
                                Matrix rotation = new Matrix();
                                rotation.postRotate(90.0F);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotation, true);
                            }
                        }

                        MyPrintHelper.PrintUriAdapter.this.mBitmap = bitmap;
                        if (bitmap != null) {
                            PrintDocumentInfo info = (new android.print.PrintDocumentInfo.Builder(MyPrintHelper.PrintUriAdapter.this.mJobName)).setContentType(1).setPageCount(1).build();
                            boolean changed = !newPrintAttributes.equals(oldPrintAttributes);
                            layoutResultCallback.onLayoutFinished(info, changed);
                        } else {
                            layoutResultCallback.onLayoutFailed((CharSequence) null);
                        }

                        MyPrintHelper.PrintUriAdapter.this.mLoadBitmap = null;
                    }

                    protected void onCancelled(Bitmap result) {
                        layoutResultCallback.onLayoutCancelled();
                        MyPrintHelper.PrintUriAdapter.this.mLoadBitmap = null;
                    }
                }).execute(new Uri[0]);
            }
        }

        void cancelLoad() {
            Object var1 = MyPrintHelper.this.mLock;
            synchronized (MyPrintHelper.this.mLock) {
                if (MyPrintHelper.this.mDecodeOptions != null) {
                    if (Build.VERSION.SDK_INT < 24) {
                        MyPrintHelper.this.mDecodeOptions.requestCancelDecode();
                    }

                    MyPrintHelper.this.mDecodeOptions = null;
                }

            }
        }

        public void onFinish() {
            super.onFinish();
            this.cancelLoad();
            if (this.mLoadBitmap != null) {
                this.mLoadBitmap.cancel(true);
            }

            if (this.mCallback != null) {
                this.mCallback.onFinish();
            }

            if (this.mBitmap != null) {
                this.mBitmap.recycle();
                this.mBitmap = null;
            }

        }

        public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
            MyPrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, fileDescriptor, cancellationSignal, writeResultCallback);
        }
    }

    @RequiresApi(19)
    private class PrintBitmapAdapter extends PrintDocumentAdapter {
        private final String mJobName;
        private final int mFittingMode;
        private final List<Bitmap> bitmaps;
        private final MyPrintHelper.OnPrintFinishCallback mCallback;
        private PrintAttributes mAttributes;

        PrintBitmapAdapter(String jobName, int fittingMode, List<Bitmap> bitmaps, MyPrintHelper.OnPrintFinishCallback callback) {
            this.mJobName = jobName;
            this.mFittingMode = fittingMode;
            this.bitmaps = bitmaps;
            this.mCallback = callback;
        }

        public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
            this.mAttributes = newPrintAttributes;
//            PrintDocumentInfo info = (new android.print.PrintDocumentInfo.Builder(this.mJobName)).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).setPageCount(bitmaps.size()).build();
//            boolean changed = !newPrintAttributes.equals(oldPrintAttributes);
//            layoutResultCallback.onLayoutFinished(info, changed);

            // Create a new PdfDocument with the requested page attributes
            // Respond to cancellation request
            if (cancellationSignal.isCanceled()) {
                layoutResultCallback.onLayoutCancelled();
                return;
            }

            // Compute the expected number of printed pages
            int pages = computePageCount(newPrintAttributes);

            if (pages > 0) {
                // Return print information to print framework
                PrintDocumentInfo info = new PrintDocumentInfo
                        .Builder("print_output.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(pages)
                        .build();
                // Content layout reflow is complete
                layoutResultCallback.onLayoutFinished(info, true);
            } else {
                // Otherwise report an error to the print framework
                layoutResultCallback.onLayoutFailed("Page count calculation failed.");
            }
        }

        private int computePageCount(PrintAttributes printAttributes) {
            return bitmaps.size();
        }

        public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
//            MyPrintHelper.this.writeBitmaps(this.mAttributes, this.mFittingMode, this.bitmaps, fileDescriptor, cancellationSignal, writeResultCallback);
            PrintedPdfDocument mPdfDocument = new PrintedPdfDocument(MyPrintHelper.this.mContext, this.mAttributes);
            for (int i = 0; i < bitmaps.size(); i++) {
                // Check to see if this page is in the output range.
                // If so, add it to writtenPagesArray. writtenPagesArray.size()
                // is used to compute the next output page index.
                PdfDocument.Page page = mPdfDocument.startPage(i);

                // check for cancellation
                if (cancellationSignal.isCanceled()) {
                    writeResultCallback.onWriteCancelled();
                    mPdfDocument.close();
                    mPdfDocument = null;
                    return;
                }

                // Draw page content for printing
                Bitmap bitmap = bitmaps.get(i);
                RectF contentRect = new RectF(page.getInfo().getContentRect());
                Matrix matrix = MyPrintHelper.getMatrix(bitmap.getWidth(), bitmap.getHeight(), contentRect, this.mFittingMode);
                if (!MyPrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                    matrix.postTranslate(contentRect.left, contentRect.top);
                    page.getCanvas().clipRect(contentRect);
                }
                page.getCanvas().drawBitmap(bitmap, matrix, (Paint) null);
                // Rendering is complete, so page can be finalized.
                mPdfDocument.finishPage(page);
            }

            // Write PDF document to file
            try {
                mPdfDocument.writeTo(new FileOutputStream(
                        fileDescriptor.getFileDescriptor()));
            } catch (IOException e) {
                writeResultCallback.onWriteFailed(e.toString());
                return;
            } finally {
                mPdfDocument.close();
                mPdfDocument = null;
            }
            PageRange[] writtenPages = new PageRange[]{PageRange.ALL_PAGES};
            // Signal the print framework the document is complete
            writeResultCallback.onWriteFinished(writtenPages);

        }


        public void onFinish() {
            if (this.mCallback != null) {
                this.mCallback.onFinish();
            }

        }
    }

    public interface OnPrintFinishCallback {
        void onFinish();
    }
}
