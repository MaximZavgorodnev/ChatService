import java.time.LocalDateTime

class Chat(val chatId: Int,//Идентификатор чата
           var chatUsers: MutableList<Message>,//Место где хранятся сообщения из чата сообщений
           val titles: String, //Название чата
           var unreadMessages: Int //количество непрочитанных сообщений
           ) {
    override fun toString(): String  {
        when {
            unreadMessages > 0 -> return "$titles индекс чата: $chatId" + "\n $chatUsers"
            else -> return  "Новых сообщений нет"
        }
    }
}
