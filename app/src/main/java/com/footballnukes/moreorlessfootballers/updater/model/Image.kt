package com.footballnukes.moreorlessfootballers.updater.model

/**
 * Created by musooff on 07/11/2018.
 */

class Image {
}

class LicenseShortName{
    var value: String? = null
}
class LicenseUrl{
    var value: String? = null
}

class Artist{
    var value: String? = null
}
class Credit{
    var value: String? = null
}

class Extmetadata{
    var LicenseShortName: LicenseShortName? = null
    var LicenseUrl: LicenseUrl? = null
    var Artist: Artist? = null
    var Credit: Credit? = null
}
class Imageinfo{
    var extmetadata: Extmetadata? = null
}