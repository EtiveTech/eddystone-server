package city4age.api

class UrlMappings {

    static mappings = {
        group("/api") {
            get "/beacon" (controller: 'beacon', action: 'index')
            get "/receiver" (controller: 'receiver', action: 'index')
            get "/receiver/$email" (controller: 'receiver', action: 'show')
            get "/event" (controller: 'event', action: 'index')
            post "/event" (controller: 'event', action: 'save')
            get "/location" (controller: 'location', action: 'index')
            get "/device" (controller: 'device', action: 'index')
            post "/device" (controller: 'device', action: 'save')
            put "/device/$uuid" (controller: 'device', action: 'update')
        }

        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}