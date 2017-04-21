package city4age.api

class BootStrap {

    def init = { servletContext ->
        new CareReceiver(emailAddress: "test01@etive.org").save()
        new CareReceiver(emailAddress: "test02@etive.org").save()
        new CareReceiver(emailAddress: "test03@etive.org").save()
        def location = new Location(name: "Chemist", type: "chemist").save()
        new Beacon(beaconId: "c4a000000001", location: location).save()
    }
    def destroy = {
    }
}