import java.time.LocalDateTime

class Message(
 val messageId: Int, //Идентификатор сообщения
 val chatId: Int, //Идентификатор чата
 val messageRecipientId: Int, //Получатель сообщения
 val messageSenderId: Int, //Отправитель сообщения
 val userNameSender: String,//Имя отправителя сообщения
 var text: String, //Текст сообщения
 val dateTime: LocalDateTime, // Дата и время создания сообщения в формате Unixtime

 var readabilityId: Boolean = false //идентификатор прочитонности сообщения(изначально оно не прочитано)
     )
 {override fun toString(): String {
  var readability = when {!readabilityId -> "!сообщение не прочитано!" + "\n " else -> ""}
        return "Сообщение от: $userNameSender" +
           "\n$dateTime" +
           "\n$text" +
           "\n " +
    "\n{Техническая информация $messageId}" +
                "\n" + "$readability"


 }
}