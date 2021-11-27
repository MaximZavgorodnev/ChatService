import java.time.LocalDateTime
import kotlin.io.println as println

object ChatService {
    var chatStorage: MutableMap<Int, Chat> = mutableMapOf()
    var idChat: Int = 0
    var idMessage: Int = 0
    var timeMessageCall: LocalDateTime = LocalDateTime.now()


    //Создаем чат
    fun createChat(titles: String, //Название чата
                   userTwo: User,//Пользователь кому пишется сообщение
                   userOne: User,//Пользователь который пишет сообщение
    ) : Chat { val newChat = Chat(chatId = idChat, chatUsers = mutableListOf(),titles = titles, unreadMessages = 0)
        chatStorage.put(newChat.chatId,newChat)
        idChat++
        userTwo.memoryOfMyChats[newChat.chatId] = userOne.userId
        userOne.memoryOfMyChats[newChat.chatId] = userTwo.userId
        return newChat
    }

    //Отправить сообщение пользователю(создать чат, сообщение и отправить его)
    fun sendMessage(userTwo: User,//Пользователь кому пишется сообщение
                    userOne: User,//Пользователь который пишет сообщение
                    text: String) //Текст сообщения
                                                {
        //Проверка на присутствие чата между пользователями
        when{
            !chatStorage.isEmpty() -> {
                when (userTwo.memoryOfMyChats.containsValue(userOne.userId)) {
                    true -> {
                        val number = userTwo.memoryOfMyChats.filterValues { it == userOne.userId}.keys
                        val chatId = chatStorage.keys.first { it == number.elementAt(0) }
                        createMessage(userTwo,userOne, text, chatId)
                    }
                    else -> {
                        println("Создан новый чат между пользователями ${userTwo.userName} и ${userOne.userName}")
                        val chat = createChat(
                            "Чат между пользователями ${userTwo.userName} и ${userOne.userName}",
                            userTwo,//Айди второго участника чата
                            userOne) //Айди первого участника чата))
                        createMessage(userTwo,userOne, text, chat.chatId)
                    }
                }
            }
            else -> {
                println("Создан новый чат между пользователями ${userTwo.userName} и ${userOne.userName}")
                    val chat = createChat("Чат между пользователями ${userTwo.userName} и ${userOne.userName}",
                    userTwo,//Айди кому написали
                    userOne)//Айди кто написал
                createMessage(userTwo,userOne, text, chat.chatId)
            }
        }
    }

    //Создание сообщения
    fun createMessage(userTwo: User,//Пользователь кому пишется сообщение
                      userOne: User,//Пользователь который пишет сообщение
                      text: String, //Текст сообщения
                      chatId: Int) {
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
        chatStorage[chatId]!!.chatUsers.add(message)
        chatStorage[chatId]!!.unreadMessages++
    }

    //Прочтение сообщения
    fun readingMessage(messageId: Int, chatId: Int){
        when(chatStorage[chatId]?.chatUsers?.first{ messageId == it.messageId }?.readabilityId){
            false -> {
                chatStorage[chatId]!!.chatUsers.first{ messageId == it.messageId }.readabilityId = true //Прочли сообщение
                println("Сообщение прочитано")
                chatStorage[chatId]!!.unreadMessages--
            }
            true -> println("Сообщение уже было прочитано")
            null -> println("Сообщения не существует")
        }
    }

    //Удалить чаты пользователя
    fun deleteChat(userTwo: User,//Пользователь чат с которым хотят удалить
                   userOne: User,//Пользователь который хочет удалить чат
                                 ) {
        when {
            !chatStorage.isEmpty() -> {
                when (userTwo.memoryOfMyChats.containsValue(userOne.userId)) {
                    true -> {
                        val number = userTwo.memoryOfMyChats.filterValues { it == userOne.userId }.keys
                        val chatId = chatStorage.keys.first { it == number.elementAt(0) }
                        chatStorage.remove(chatId)
                        println("Чат между пользователями ${userTwo.userName} и ${userOne.userName} удален")
                    }
                    else -> { println("Чата между пользователями ${userTwo.userName} и ${userOne.userName} не существует") }
                }
            }
            else -> { println("Чата между пользователями ${userTwo.userName} и ${userOne.userName} не существует") }
        }
    }

    //Вывести все чаты пользователя
    fun outputChats(userOne: User): Collection<Chat>{
        when {!chatStorage.isEmpty() -> {
            val number = userOne.memoryOfMyChats.keys
            val chats = chatStorage.filter { number.contains(it.component1()) }.values
            return chats
            }
            else -> {throw ChatNotFoundException("У пользователя ${userOne.userName} нет чатов")}
        }
    }

    //Получить список сообщений из чата
    fun getListOfMessages(userOne: User, idChat: Int): MutableList<Message> {
        val userOneChat = outputChats(userOne)
        val idChats = idChat
//            (0..idChat).shuffled().last()
        val chat = userOneChat.first {it.chatId == idChats}.chatUsers
        return chat
    }

    //Отредактировать сообщение пользователя в этом чате
    fun editMessageInChat(userOne: User, text: String, id: Int){
        val idChats = id
//            (0..idChat).shuffled().last()
        val idMessages = 1
//            (0..idMessage).shuffled().last()
        val chat = getListOfMessages(userOne, idChats)
//        println(chat)
        when (chatStorage[idChats] != null){
            true -> chatStorage[idChats]!!.chatUsers.first { it.messageId == idMessages }.text = text
            else -> throw ChatNotFoundException("У пользователя ${userOne.userName} таких сообщений нет")
        }
    }


    //Удалить сообщение пользователя в этом чате(при удалении последнего сообщения в чате весь чат удаляется)
    fun deleteMessage(userOne: User, chatId: Int){
       when(chatStorage[chatId]?.chatUsers?.removeAll { chat -> chat.messageRecipientId == userOne.userId || chat.messageSenderId == userOne.userId }) {
           true -> println("Чат удален")
           false -> println("Такого чата не существует")
           null -> println("Такого чата не существует")
       }
    }

    //Получить информацию о количестве непрочитанных чатов (например, service.getUnreadChatsCount) -
    // это количество чатов, в каждом из которых есть хотя бы одно непрочитанное сообщение
    fun getUnreadChatsCount(): Int {
        val unreadChat = chatStorage.values.filter { chat -> chat.unreadMessages !=0 }
        return unreadChat.size
    }

    //Получить список чатов пользователя - где в каждом чате есть последнее сообщение (если нет, то пишется "нет сообщений")
    fun getListOfChatsWithMessages(userOne: User): Collection<Chat> {
        val number = userOne.memoryOfMyChats.keys
        return chatStorage.filter { number.contains(it.component1()) }.values }



}