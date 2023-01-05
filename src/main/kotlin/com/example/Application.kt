package com.example

import io.ktor.server.application.*
import io.ktor.server.netty.*
import com.example.plugins.*
import de.sharpmind.ktor.EnvConfig


//fun main() {
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)
//}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    val port = environment.config.propertyOrNull("envConfig.student_queue")?.getString() ?: "TESTTESTESTESTESTTEST"
    println(port)
    EnvConfig.initConfig(environment.config)
    configureSerialization()
    configureRouting()
    endpoints()
}
