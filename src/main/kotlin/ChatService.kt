import java.time.LocalDateTime
import kotlin.io.println as println

object ChatService {
    var chatStorage: MutableMap<Int, Chat> = mutableMapOf()
    var idChat: Int = 0
    var idMessage: Int = 0
    var timeMessageCall: LocalDateTime = LocalDateTime.now()


    //Отправить сообщение пользователю(создать чат, сообщение и отправить его)
    fun sendMessage(userTwo: User,//Пользователь кому пишется сообщение
                    userOne: User,//Пользователь который пишет сообщение
                    text: String) //Текст сообщения
    {
       //Написание сообщения по новому
        try {
            val chatId = userTwo.memoryOfMyChats.filter { userTwo.memoryOfMyChats.containsKey(userOne.userId) }.getValue(userOne.userId)

            var message = Message(
                messageId = idMessage , //Идентификатор сообщения
                chatId = chatId, //Идентификатор чата
                messageRecipientId = userTwo.userId, //Получатель сообщения
                messageSenderId = userOne.userId, //Отправитель сообщения
                userNameSender = userOne.userName,//Имя отправителя сообщения
                text = text, //Текст сообщения
                dateTime = timeMessageCall, // Дата и время создания сообщения в формате Unixtime
                readabilityId = false )
            idMessage++
            chatStorage.getValue(chatId).chatUsers.add(message)
            chatStorage.getValue(chatId).unreadMessages++
            println("Сообщение отправлено")
        } catch (e: NoSuchElementException){
//            println("Чата между ними нет")
            println("Создан новый чат между пользователями ${userTwo.userName} и ${userOne.userName}")
            val newChat = Chat(chatId = idChat, chatUsers = mutableListOf(),titles = "Чат между пользователями ${userTwo.userName} и ${userOne.userName}", unreadMessages = 0)
            chatStorage.put(newChat.chatId, newChat)

            userTwo.memoryOfMyChats[userOne.userId] = newChat.chatId
            userOne.memoryOfMyChats[userTwo.userId] = newChat.chatId
            var message = Message(
                messageId = idMessage , //Идентификатор сообщения
                chatId = idChat, //Идентификатор чата
                messageRecipientId = userTwo.userId, //Получатель сообщения
                messageSenderId = userOne.userId, //Отправитель сообщения
                userNameSender = userOne.userName,//Имя отправителя сообщения
                text = text, //Текст сообщения
                dateTime = timeMessageCall, // Дата и время создания сообщения в формате Unixtime
                readabilityId = false )
            chatStorage.getValue(idChat).chatUsers.add(message)
            chatStorage.getValue(idChat).unreadMessages++
            idMessage++
            idChat++
        }
    }

    //Прочтение сообщения
    fun readingMessage(messageId: Int, chatId: Int){
        try {
            chatStorage.filter { !chatStorage.getValue(chatId).chatUsers.first { messageId == it.messageId }
                .readabilityId }.getValue(chatId).chatUsers.first { messageId == it.messageId }.readabilityId = true
            println("Cообщение прочитано")
        } catch (e: NoSuchElementException){
            println("Cообщение не существует")
        }
    }

    //Удалить чаты пользователя
    fun deleteChat(userTwo: User,//Пользователь чат с которым хотят удалить
                   userOne: User,//Пользователь который хочет удалить чат
    ) {
        try {
            chatStorage.remove(userOne.memoryOfMyChats[userTwo.userId])
        } catch (e: NoSuchElementException){
            println("Чата между пользователями не существует")
        }
    }

    //Вывести все чаты пользователя
    fun outputChats(userOne: User): Collection<Chat>{
        return  chatStorage.filter{ !userOne.memoryOfMyChats.isEmpty() }.filter{ userOne.memoryOfMyChats.values.contains(it.component1()) }.values
    }

    //Получить список сообщений из чата
    fun getListOfMessages(userOne: User, idChat: Int): MutableList<Message> {
        //            (0..idChat).shuffled().last()
        try {
            val chat = outputChats(userOne).first {it.chatId == idChat}.chatUsers
            return chat
        } catch (e: Exception) {
            throw ChatNotFoundException("У пользователя ${userOne.userName} нет чатов")
        }
    }

    //Отредактировать сообщение пользователя в этом чате
    fun editMessageInChat(userOne: User, text: String, idChat: Int){
//            (0..idChat).shuffled().last()
        val idMessages = 1
//            (0..idMessage).shuffled().last()
        try {
            chatStorage.getValue(idChat).chatUsers.first { it.messageId == idMessages }.text = text
        } catch (e: NoSuchElementException){
            println("У пользователя ${userOne.userName} нет чатов")
        }
    }


    //Удалить сообщения пользователя в этом чате(при удалении последнего сообщения в чате весь чат удаляется)
    fun deleteMessage(userOne: User, chatId: Int): Boolean {
        return try {
            chatStorage.getValue(chatId).chatUsers.removeAll{ message -> message.messageRecipientId == userOne.userId || message.messageSenderId == userOne.userId }
            println("Сообщения удалены")
            true
        } catch (e: NoSuchElementException) {
            println("Такого чата не существует")
            false
        }
    }

    //Получить информацию о количестве непрочитанных чатов (например, service.getUnreadChatsCount) -
    // это количество чатов, в каждом из которых есть хотя бы одно непрочитанное сообщение
    fun getUnreadChatsCount(): Int {
        return chatStorage.values.filter { chat -> chat.unreadMessages !=0 }.size
    }

    //Получить список чатов пользователя - где в каждом чате есть последнее сообщение (если нет, то пишется "нет сообщений")
    fun getListOfChatsWithMessages(userOne: User): Collection<Chat> {
        return chatStorage.filter { userOne.memoryOfMyChats.keys.contains(it.component1()) }.values
    }
}