package com.laolang.lightserver.util

import com.laolang.lightserver.connector.http.Constants
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith

class StringManagerTest : DescribeSpec({

    lateinit var sm: StringManager

    beforeSpec {
        sm = StringManager.getManager(Constants.Package)
    }

    describe("StringManager 基础功能测试") {

        it("测试单例获取") {
            StringManager.getManager(Constants.Package) shouldBe StringManager.getManager(Constants.Package)
        }

        it("正常无参") {
            sm.getString("httpConnector.alreadyInitialized") shouldBe "HTTP connector has already been initialized"
        }

        it("正常有参") {
            sm.getString("httpConnector.anAddress", "localhost") shouldBe "Opening server socket on host IP address localhost"
        }

        it("正常两个参数") {
            sm.getString("test.with.two.arg", "1", "2") shouldBe "hello 1 2"
        }

        it("正常三个参数") {
            sm.getString("test.with.three.arg", "1", "2", "3") shouldBe "hello 1 2 3"
        }

        it("正常四个参数") {
            sm.getString("test.with.four.arg", "1", "2", "3", "4") shouldBe "hello 1 2 3 4"
        }

        it("无参空参数 - 预期抛出 NullPointerException") {
            shouldThrow<NullPointerException> {
                sm.getString(null)
            }
        }

        it("不存在的配置") {
            sm.getString("notExist") shouldStartWith "Cannot find message associated with key"
        }

        it("有参且有 1 个空参数") {
            sm.getString("test.with.two.arg", "1", null) shouldBe "hello 1 null"
        }

        it("有参且有多个空参数") {
            sm.getString("test.with.four.arg", "1", null, null, "4") shouldBe "hello 1 null null 4"
        }

        it("有参降级") {
            sm.getString("test.with.number.arg", "xx") shouldBe "hello {0,number,interger} arg[0]=xx"
        }
    }

})