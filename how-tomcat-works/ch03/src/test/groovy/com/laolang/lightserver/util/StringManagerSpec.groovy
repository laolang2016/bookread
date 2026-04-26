package com.laolang.lightserver.util

import com.laolang.lightserver.connector.http.Constants
import spock.lang.Shared
import spock.lang.Specification

class StringManagerSpec extends Specification {

    @Shared
    StringManager sm

    def setupSpec() {
        sm = StringManager.getManager(Constants.Package)
    }

    def "测试单例获取"() {
        expect:
        StringManager.getManager(Constants.Package) == StringManager.getManager(Constants.Package)
    }

    def "正常无参"() {
        expect:
        sm.getString("httpConnector.alreadyInitialized") == "HTTP connector has already been initialized"
    }

    def "正常有参"() {
        expect:
        sm.getString("httpConnector.anAddress", "localhost") == "Opening server socket on host IP address localhost"
    }

    def "正常两个参数"() {
        expect:
        sm.getString("test.with.two.arg", "1", "2") == "hello 1 2"
    }

    def "正常三个参数"() {
        expect:
        sm.getString("test.with.three.arg", "1", "2", "3") == "hello 1 2 3"
    }

    def "正常四个参数"() {
        expect:
        sm.getString("test.with.four.arg", "1", "2", "3", "4") == "hello 1 2 3 4"
    }

    def "无参空参数"() {
        when:
        sm.getString(null)
        then:
        thrown(NullPointerException)
    }

    def "不存在的配置"() {
        expect:
        sm.getString("notExist").startsWith("Cannot find message associated with key")
    }

    def "有参且有 1 个空参数"() {
        expect:
        sm.getString("test.with.two.arg", "1", null) == "hello 1 null"
    }

    def "有参且有多个空参数"() {
        expect:
        sm.getString("test.with.four.arg", "1", null, null, "4") == "hello 1 null null 4"
    }

    def "有参降级"() {
        expect:
        sm.getString("test.with.number.arg", "xx") == "hello {0,number,interger} arg[0]=xx"
    }
}
