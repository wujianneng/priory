package com.pos.priory.beans;

import java.util.List;

public class ExchangeCashCouponBean {


    /**
     * code : 200
     * msg : OK
     * level : info
     * result : {"member_reward":6,"reward_list":[{"id":1,"name":"100分換100元","reducereward":100,"value":"100.00"}]}
     */

    private int code;
    private String msg;
    private String level;
    private ResultBean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * member_reward : 6
         * reward_list : [{"id":1,"name":"100分換100元","reducereward":100,"value":"100.00"}]
         */

        private int member_reward;
        private List<RewardListBean> reward_list;

        public int getMember_reward() {
            return member_reward;
        }

        public void setMember_reward(int member_reward) {
            this.member_reward = member_reward;
        }

        public List<RewardListBean> getReward_list() {
            return reward_list;
        }

        public void setReward_list(List<RewardListBean> reward_list) {
            this.reward_list = reward_list;
        }

        public static class RewardListBean {
            /**
             * id : 1
             * name : 100分換100元
             * reducereward : 100
             * value : 100.00
             */

            private int id;
            private String name;
            private int reducereward;
            private String value;
            boolean isSelected = false;
            int count = 0;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getReducereward() {
                return reducereward;
            }

            public void setReducereward(int reducereward) {
                this.reducereward = reducereward;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
