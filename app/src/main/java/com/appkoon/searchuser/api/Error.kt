package com.appkoon.searchuser.api

enum class Error(val value: String) {
    NO_DATA("검색결과가 없습니다."),
    NO_MORE_DATA("마지막 페이지 입니다."),
    TIMEOUT("연결시간이 초과되었습니다."),
    DISCONNECTED("네트워트에 연결되어 있지 않습니다."),
    UNKNOWN("서버오류가 발생되었습니다.")
}
