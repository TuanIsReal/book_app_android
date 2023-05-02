package com.anhtuan.bookapp.config;

public class Constant {
    public static final String IP_SERVER = "http://192.168.1.19:8080/";

    //ip Hai
//    public static final String IP_SERVER = "http://192.168.43.143:8080/";
    //ip cau
//    public static final String IP_SERVER = "http://192.168.90.197:8080/";
    // ip tuan
//    public static final String IP_SERVER = "http://192.168.130.143:8080/";

    public static class AddBookType{
        public static final int MEMBER_ADD = 1;
        public static final int ADMIN_ADD = 2;
    }

    public class ReactUpBookRequest{
        public static final int REJECT = 0;
        public static final int ACCEPT = 1;
    }

    public class StatusBookRequestUp{
        public static final int REQUEST = 0;
        public static final int ACCEPTED = 1;
        public static final int REJECTED = -1;
    }

    public class TYPE_FILTER{
        public static final int NEW_BOOK = 1;
        public static final int RECOMMEND_BOOK = 2;
        public static final int MOST_BUY = 3;
        public static final int MOST_REVIEW = 4;
    }

    public class FILTER_STATUS{
        public static final int WRITING = 1;
        public static final int COMPLETE = 2;
    }

    public class FILTER_POST{
        public static final int USER_POST = 1;
        public static final int ADMIN_POST = 2;
    }

    public class FILTER_SORT{
        public static final int SORT_BY_TIME = 1;
        public static final int SORT_BY_PURCHASED = 2;
        public static final int SORT_BY_REVIEW = 3;
        public static final int SORT_BY_STAR = 4;
        public static final int SORT_BY_CHAPTER = 5;
        public static final int SORT_BY_PRICE = 6;
    }
}
