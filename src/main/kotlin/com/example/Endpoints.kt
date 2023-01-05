package com.example

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.endpoints(){

    routing{
        get("/students"){
            val rabbitMQClient = RabbitService()
            val jsonObject = jacksonObjectMapper().writeValueAsString(mapOf(
                "action" to "get",
                "data" to { }
            ))
            val response: String = rabbitMQClient.retrieveAllStudents(jsonObject)
            rabbitMQClient.close()

            val jsonResponse = jacksonObjectMapper().readValue<Any>(response)

            call.respond(jsonResponse)
        }

        get("/students/{id}"){
            val id = call.parameters["id"]?.toInt()
            val rabbitMQClient = RabbitService()
            val jsonObject = jacksonObjectMapper().writeValueAsString(mapOf(
                "action" to "get",
                "data" to mapOf("id" to id)
            ))
            val response: String = rabbitMQClient.retrieveAllStudents(jsonObject)
            rabbitMQClient.close()

            val jsonResponse = jacksonObjectMapper().readValue<Any>(response)

            call.respond(jsonResponse)
        }
    }

}