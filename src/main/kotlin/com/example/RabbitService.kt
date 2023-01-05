package com.example

import com.rabbitmq.client.*
import de.sharpmind.ktor.EnvConfig
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

class RabbitService {
    private val connectionFactory: ConnectionFactory = ConnectionFactory()
    private var connection: Connection
    private var channel: Channel
    private val replyQueueName: String
    private val requestQueueName = EnvConfig.getString("student_queue")

    init {
        connectionFactory.setUri(EnvConfig.getString("connectionstringrabbitmq"))
        connection = connectionFactory.newConnection()
        channel = connection.createChannel()
        replyQueueName = channel.queueDeclare().queue
    }

    fun giveFactory(): ConnectionFactory{
        return connectionFactory
    }

    fun retrieveAllStudents(message: String): String{
        val corrId = UUID.randomUUID().toString()

        val props = AMQP.BasicProperties.Builder()
            .correlationId(corrId)
            .replyTo(replyQueueName)
            .build()

        channel.basicPublish("", requestQueueName, props, message.toByteArray(charset("UTF-8")))

        val response = ArrayBlockingQueue<String>(1)

        channel.basicConsume(replyQueueName, true, object : DefaultConsumer(channel) {
            override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray){
                if (properties.correlationId == corrId){
                    response.offer(String(body, charset("UTF-8")))
                }
            }
        })

        return response.take()
    }

    fun close(){
        connection.close()
    }

}