package com.example.lulin.todolist.Bean;

import java.util.List;

/**
 * Created by Lulin on 2018/9/3.
 */

public class BaiDuVoice {

    /**
     * results_recognition : ["语音识别"]
     * origin_result : {"corpus_no":6475907806490892620,"err_no":0,"result":{"word":["语音识别"]},"sn":"c006252d-2993-4982-a363-2628053181cc_s-0"}
     * error : 0
     * best_result : 语音识别
     * result_type : final_result
     */

    public OriginResultBean origin_result;
    public int error;
    public String best_result;
    public String result_type;
    public List<String> results_recognition;

    public static class OriginResultBean {
        /**
         * corpus_no : 6475907806490892620
         * err_no : 0
         * result : {"word":["语音识别"]}
         * sn : c006252d-2993-4982-a363-2628053181cc_s-0
         */

        public long corpus_no;
        public int err_no;
        public ResultBean result;
        public String sn;

        public static class ResultBean {
            public List<String> word;
        }

        @Override
        public String toString() {
            return "OriginResultBean{" +
                    "corpus_no=" + corpus_no +
                    ", err_no=" + err_no +
                    ", result=" + result +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BaiDuVoice{" +
                "origin_result=" + origin_result +
                ", error=" + error +
                ", best_result='" + best_result + '\'' +
                ", result_type='" + result_type + '\'' +
                ", results_recognition=" + results_recognition +
                '}';
    }
}
