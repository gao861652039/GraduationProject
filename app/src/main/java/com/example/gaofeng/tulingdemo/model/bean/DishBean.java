package com.example.gaofeng.tulingdemo.model.bean;

import java.util.List;

/**
 * Created by gaofeng on 2017/12/15.
 */

public class DishBean {


    /**
     * code : 308000
     * text : 亲，已帮您找到菜谱信息
     * list : [{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/f3067a6e886111e6b87c0242ac110003_640w_640h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分8.3 16167人下厨","detailurl":"http://m.xiachufang.com/recipe/100352761/?ref=tuling"},{"name":"史上最详尽经典川菜【鱼香肉丝】","icon":"http://s2.cdn.xiachufang.com/f85bc60287be11e6a9a10242ac110002_1500w_1004h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分8.6 4474人下厨","detailurl":"http://m.xiachufang.com/recipe/1102796/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/a09b8a0a890911e6b87c0242ac110003_650w_650h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分8.3 393人下厨","detailurl":"http://m.xiachufang.com/recipe/100489447/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/1a974eba874911e6a9a10242ac110002_600w_450h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分8.4 13109人下厨","detailurl":"http://m.xiachufang.com/recipe/1010097/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/b89670e8873311e6a9a10242ac110002_690w_459h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分8.2 6469人下厨","detailurl":"http://m.xiachufang.com/recipe/261379/?ref=tuling"},{"name":"日食记【鱼香肉丝】","icon":"http://s2.cdn.xiachufang.com/5945def488ea11e6a9a10242ac110002_598w_292h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分8.9 62人下厨","detailurl":"http://m.xiachufang.com/recipe/100463707/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/21fec6428b5911e6b87c0242ac110003_800w_533h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分8.5 21人下厨","detailurl":"http://m.xiachufang.com/recipe/101853005/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/3b4fd10681f211e6b87c0242ac110003_800w_533h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分7.5 403人下厨","detailurl":"http://m.xiachufang.com/recipe/100218709/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/a79c6cf2fb2311e6bc9d0242ac110002_1280w_1280h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分8.2 121人下厨","detailurl":"http://m.xiachufang.com/recipe/102214342/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/ccae4340896c11e6b87c0242ac110003_690w_919h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分8.2 38人下厨","detailurl":"http://m.xiachufang.com/recipe/100553600/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/7b5f8c30873511e6b87c0242ac110003_680w_452h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分78人下厨","detailurl":"http://m.xiachufang.com/recipe/264781/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/969d77ba880611e6a9a10242ac110002_500w_753h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分34人下厨","detailurl":"http://m.xiachufang.com/recipe/100046438/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/232457d0874411e6a9a10242ac110002_3000w_2000h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分50人下厨","detailurl":"http://m.xiachufang.com/recipe/1007140/?ref=tuling"},{"name":"鱼香肉丝盖饭","icon":"http://s1.cdn.xiachufang.com/2f78602e874111e6a9a10242ac110002_800w_600h.jpg@2o_50sh_1pr_1l_280w_190h_1c_1e_90q_1wh","info":"评分8.2 60人下厨","detailurl":"http://m.xiachufang.com/recipe/1005577/?ref=tuling"},{"name":"鱼香肉丝","icon":"http://s2.cdn.xiachufang.com/7b74719c88af11e6a9a10242ac110002_650w_650h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90","info":"评分20人下厨","detailurl":"http://m.xiachufang.com/recipe/100410514/?ref=tuling"}]
     */

    private int code;
    private String text;
    private List<ListBean> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * name : 鱼香肉丝
         * icon : http://s2.cdn.xiachufang.com/f3067a6e886111e6b87c0242ac110003_640w_640h.jpg?imageView2/1/w/280/h/190/interlace/1/q/90
         * info : 评分8.3 16167人下厨
         * detailurl : http://m.xiachufang.com/recipe/100352761/?ref=tuling
         */

        private String name;
        private String icon;
        private String info;
        private String detailurl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }
    }
}
