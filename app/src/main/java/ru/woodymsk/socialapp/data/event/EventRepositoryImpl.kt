package ru.woodymsk.socialapp.data.event

import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.AttachmentType.IMAGE
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.domain.post.model.EventsItem
import ru.woodymsk.socialapp.domain.event.EventRepository
import withContextIO
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor() : EventRepository {

    override suspend fun getAllEventList(): List<EventsItem> = withContextIO {
        mockEvents
    }
}

private val mockEvents = listOf(
    EventsItem(
        415,
        261,
        "Антон",
        null,
        null,
        "2342",
        "2023-07-21T20:53:03.000005Z",
        "2023-07-19T20:52:58.410057Z",
        Coords(
            "61.465297",
            "88.462070"
        ),
        "OFFLINE",
        listOf(
            293
        ),
        false,
        listOf(
            372
        ),
        listOf(),
        false,
        Attachment(
            "https://avatars.dzeninfra.ru/get-zen_doc/1591747/pub_62096591fb1dc452d563288e_620965adf3c7ee2a9dd35a56/scale_1200",
            IMAGE
        ),
        null,
        false,
    ),
    EventsItem(
        413,
        261,
        "Антон",
        null,
        null,
        "123 (edit)",
        "2023-07-11T03:24:54.000002Z",
        "2023-07-19T18:13:51.659260Z",
        Coords(
            "59.053920",
            "77.857117"
        ),
        "ONLINE",
        listOf(
            293
        ),
        false,
        listOf(
            376,
            374
        ),
        listOf(261),
        false,
        null,
        "www.hi.ru",
        false,
    ),
    EventsItem(
        384,
        288,
        "xxx",
        "https://i.pinimg.com/originals/a9/a0/1a/a9a01a1e598b1ba81b69b9fba71c63af.jpg",
        null,
        "Android UI : Fixing skipped frames.\n\nFixing this requires identifying nodes where there is or possibly can happen long duration of processing. The best way is to do all the processing no matter how small or big in a thread separate from main UI thread. So be it accessing data form SQLite Database or doing some hardcore maths or simply sorting an array – Do it in a different thread.\n\nNow there is a catch here, You will create a new Thread for doing these operations and when you run your application, it will crash saying “Only the original thread that created a view hierarchy can touch its views“. You need to know this fact that UI in android can be changed by the main thread or the UI thread only. Any other thread which attempts to do so, fails and crashes with this error. What you need to do is create a new Runnable inside runOnUiThread and inside this runnable you should do all the operations involving the UI. Find an example here.\n\nSo we have Thread and Runnable for processing data out of main Thread, what else? There is AsyncTask in android which enables doing long time processes on the UI thread. This is the most useful when you applications are data driven or web api driven or use complex UI’s like those build using Canvas. The power of AsyncTask is that is allows doing things in background and once you are done doing the processing, you can simply do the required actions on UI without causing any lagging effect. This is possible because the AsyncTask derives itself from Activity’s UI thread – all the operations you do on UI via AsyncTask are done is a different thread from the main UI thread, No hindrance to user interaction.\n\nSo this is what you need to know for making smooth android applications and as far I know every beginner gets this message on his console.",
        "2023-07-19T05:30:00Z",
        "2023-07-15T07:42:48.027819Z",
        null,
        "ONLINE",
        listOf(
            372
        ),
        false,
        listOf(),
        listOf(
            288
        ),
        false,
        Attachment(
            "https://i.pinimg.com/originals/29/a3/b6/29a3b6efcc703a07a57b13a026b5c83f.jpg",
            IMAGE
        ),
        null,
        false,
    ),
    EventsItem(
        221,
        196,
        "Monke",
        "https://i.pinimg.com/originals/98/79/6b/98796b3c7f390716494031249829e88d.jpg",
        "Big zoo",
        "Another event. Fun!\nTrying a little edit",
        "2023-04-25T14:00:01Z",
        "2023-04-20T20:27:12.853344Z",
        Coords(
            "55.750584",
            "37.624288"
        ),
        "ONLINE",
        listOf(
            196
        ),
        false,
        listOf(
            201,
            202,
            196,
            197
        ),
        listOf(
            145,
            202,
            99,
            196
        ),
        false,
        null,
        "https://yandex.ru/maps/213/moscow/?ll=37.625227%2C55.749454&z=17.85",
        false,
    ),
    EventsItem(
        142,
        136,
        "иван иванов",
        "https://i.pinimg.com/originals/2f/50/b5/2f50b5e95782501a11a2e7cf98abf89b.jpg",
        null,
        "что то такое непонятное творится",
        "2023-03-16T00:42:00Z",
        "2023-03-11T12:42:50.003299Z",
        null,
        "ONLINE",
        listOf(137),
        false,
        listOf(),
        listOf(137),
        false,
        Attachment(
            "https://fikiwiki.com/uploads/posts/2022-02/1644991819_28-fikiwiki-com-p-prikolnie-kartinki-pro-kotov-29.jpg",
            IMAGE
        ),
        null,
        false,
    )
)