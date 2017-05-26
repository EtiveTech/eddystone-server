package org.etive.city4age.repository

class RegionController {
	
    def index() {
        def list = Region.forDownload()
        if (!params.stamp || params.stamp.toLong() < list.lastChange)
            respond (list, status: 200)
        else
            render status: 304

    }
}
