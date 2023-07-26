package ru.woodymsk.socialapp.data.post_screen

import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.data.model.PostsItem
import ru.woodymsk.socialapp.domain.post_screen.PostRepository
import withContextIO
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor() : PostRepository {

    override suspend fun getAllPostList(): List<PostsItem> = withContextIO {
        mockPosts
    }
}

private val mockPosts = listOf(
    PostsItem(
        839,
        261,
        "Антон",
        null,
        null,
        "Это текст ни о чём. Серьёзно, не ищите в нём какие-то скрытые смыслы или интересную информацию — их попросту нет. Вообще нет. И между строк копаться бесполезно — там пусто. Обычные белые полосы. Я вас уверяю, даже в соседней бородатой шутке или баянистом меме больше смысла, чем в этих словах. Поэтому листайте дальше, проходите мимо. Не замечайте этот текст, игнорируйте его, как можно скорее забудьте. Искренне надеюсь, что вы доверяете мне, поэтому уже давно ушли и не пытаетесь дочитать до конца, ведь впереди такая же пустота, как и в начале. Честно. Вы просто зря потратите время. Это самый пустой текст, который вы когда-либо видели. Хочется верить, что эти строчки уже никто не прочтёт. ",
        "2023-07-19T17:58:27.110642Z",
        Coords(
            "63.075186",
            "108.404590"
        ),
        "hi.ru",
        listOf(),
        listOf(),
        false,
        likedByMe = false,
        attachment = Attachment(
            "https://gas-kvas.com/uploads/posts/2023-02/1675415680_gas-kvas-com-p-kartinki-dlya-fonovogo-risunka-rabochego-s-25.jpg",
            "IMAGE"
        ),
        ownedByMe = false,
    ),
    PostsItem(
        1838,
        261,
        "Антон",
        "https://gas-kvas.com/uploads/posts/2023-01/1673526810_gas-kvas-com-p-detskii-risunok-shrek-21.jpg",
        null,
        "3435",
        "2023-07-19T17:56:55.425441Z",
        null,
        null,
        listOf(372),
        listOf(
            372,
            375
        ),
        mentionedMe = false,
        likedByMe = false,
        attachment = null,
        ownedByMe = false,
    ),
    PostsItem(
        1836,
        293,
        "Bob",
        "https://i.pinimg.com/originals/44/bd/b9/44bdb958ca542da0348c607004b1007c.jpg",
        "Second Job",
        "fhjffh",
        "2023-07-17T10:08:43.803561Z",
        Coords(
            "10.000000",
            "10.000000"
        ),
        "www.hi.ru",
        listOf(
            372,
            293
        ),
        listOf(
            293
        ),
        mentionedMe = false,
        likedByMe = false,
        attachment = Attachment(
            "https://fikiwiki.com/uploads/posts/2022-02/1644890416_36-fikiwiki-com-p-leopardi-krasivie-kartinki-40.jpg",
            "IMAGE"
        ),
        ownedByMe = false,
    ),
    PostsItem(
        1591,
        293,
        "Bob",
        "https://i01.fotocdn.net/s108/9eb8b4fa783b39f4/public_pin_l/2364152508.jpg",
        "Second Job",
        "Musik",
        "2023-06-13T15:42:13.682625Z",
        Coords(
            "10.000000",
            "10.000000"
        ),
        "www.hi.ru",
        listOf(
            293
        ),
        listOf(
            264,
            324,
            293,
            332
        ),
        mentionedMe = false,
        likedByMe = false,
        attachment = Attachment(
            "https://ik.imagekit.io/jwudrxfj5ek/0c810b42-1082-4b1b-a679-58e7f08afc46._22u9R3jxX.mp3",
            "AUDIO"
        ),
        ownedByMe = false,
    ),
    PostsItem(
        761,
        169,
        "dinosaurs",
        "https://i.pinimg.com/originals/03/6c/3c/036c3c3cc6bc299af3426d3aa3f0f737.jpg",
        "dinoф",
        "image post",
        "2023-04-05T19:22:34.444919Z",
        null,
        null,
        listOf(),
        listOf(),
        false,
        likedByMe = false,
        attachment = Attachment(
            "https://mobimg.b-cdn.net/v3/fetch/60/60b05e1e1307312c653cfe8c183f6e0d.jpeg?w=1470&r=0.5625",
            "IMAGE"
        ),
        ownedByMe = false,
    ),
)