package com.demo.strongbox.fire

object Local0914Conf {
    const val E=""
    const val U=""

    const val LOCAL_RAO_0914="""{
    "state":0,
    "name":[
        "com.UCMobile"
    ]
}"""

    val server0914List= arrayListOf(
        Server0914En(
            host_0914_strong = "100.223.52.0",
            port_0914_strong = 100,
            pwd_0914_strong = "123456",
            country_0914_strong = "Japan",
            city_0914_strong = "Tokyo",
            method_0914_strong = "chacha20-ietf-poly1305",
        ),
        Server0914En(
            host_0914_strong = "100.223.52.78",
            port_0914_strong = 100,
            pwd_0914_strong = "123456",
            country_0914_strong = "Japan",
            city_0914_strong = "Tokyo",
            method_0914_strong = "chacha20-ietf-poly1305",
        ),
    )
}