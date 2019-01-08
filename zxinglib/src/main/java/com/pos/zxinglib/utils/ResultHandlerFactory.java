/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pos.zxinglib.utils;

import android.app.Activity;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;

/**
 * Manufactures Android-specific handlers based on the barcode content's type.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ResultHandlerFactory {
  private ResultHandlerFactory() {
  }

  public static String makeResultHandler(Activity activity, Result rawResult) {
    ParsedResult result = parseResult(rawResult);
    ParsedResultType type = result.getType();
    if (type.equals(ParsedResultType.ADDRESSBOOK)) {
      return "ADDRESSBOOK";
    } else if (type.equals(ParsedResultType.EMAIL_ADDRESS)) {
    	return "EMAIL_ADDRESS";
    } else if (type.equals(ParsedResultType.PRODUCT)) {
    	return "PRODUCT";
    } else if (type.equals(ParsedResultType.URI)) {
    	return "URI";
    } else if (type.equals(ParsedResultType.WIFI)) {
    	return "WIFI";
    } else if (type.equals(ParsedResultType.TEXT)) {
    	return "TEXT";
    } else if (type.equals(ParsedResultType.GEO)) {
      return "GEO";
    } else if (type.equals(ParsedResultType.TEL)) {
    	return "TEL";
    } else if (type.equals(ParsedResultType.SMS)) {
      return "SMS";
    } else if (type.equals(ParsedResultType.CALENDAR)) {
    	return "CALENDAR";
    } else if (type.equals(ParsedResultType.ISBN)) {
      return "ISBN";
    } else {
      // The TextResultHandler is the fallthrough for unsupported formats.
      return "TEXT";
    }
  }

  private static ParsedResult parseResult(Result rawResult) {
    return ResultParser.parseResult(rawResult);
  }
}
