package ru.woodymsk.socialapp.data.post_screen

import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.data.model.PostsItem
import ru.woodymsk.socialapp.domain.post_screen.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor() : PostRepository {
    override lateinit var posts: List<PostsItem>

    override suspend fun getAllPostList() {
        posts = mockPosts
    }
}

private val mockPosts = listOf(
    PostsItem(
        839,
        261,
        "Антон",
        null,
        null,
        "222221 (edit) ff",
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
            "https://ik.imagekit.io/jwudrxfj5ek/6e60148f-e481-4825-ab1b-319e6e1ff6eb._qXwSGw8G6.jpg",
            "IMAGE"
        ),
        ownedByMe = false,
    ),
    PostsItem(
        1838,
        261,
        "Антон",
        "https://ik.imagekit.io/jwudrxfj5ek/f8821727-b435-43cb-b8db-b9067dd7dade._fLos7hAiy.jpg",
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
        "https://ik.imagekit.io/jwudrxfj5ek/e3b41df6-a246-4446-8151-dc50d0b85cec._BFw4z_zws.jpg",
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
            "https://ik.imagekit.io/jwudrxfj5ek/f7744fea-a238-4d85-a338-8e4c504e3f2b._6ZqA0doiw.png",
            "IMAGE"
        ),
        ownedByMe = false,
    ),
    PostsItem(
        1591,
        293,
        "Bob",
        "https://ik.imagekit.io/jwudrxfj5ek/b506b2cc-ec3e-40b6-b489-2bab4ab9924a._W39DAV9K0.jpg",
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
        "https://ik.imagekit.io/jwudrxfj5ek/79251fbf-1757-46dc-96ab-6e465e2bff66._1AF9McJU-.jpg",
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
            "https://ik.imagekit.io/jwudrxfj5ek/3911a780-56b5-4f4c-8975-bd9d43197678._efniL9VfA.png",
            "IMAGE"
        ),
        ownedByMe = false,
    ),
)