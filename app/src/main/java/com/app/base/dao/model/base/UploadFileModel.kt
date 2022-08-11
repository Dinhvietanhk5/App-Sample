package com.app.base.dao.model.base

class UploadFileModel : ResponeBase() {
    /**
    {
    "error": false,
    "upload": "https://vufoodads.s3.ap-southeast-1.amazonaws.com/vufood/ads/2021/06/04/26081faf15f53c284db7d8489e372c8b.?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJWV2UWBGOU7M53TA%2F20210604%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Date=20210604T092446Z&X-Amz-SignedHeaders=host&X-Amz-Expires=1200&X-Amz-Signature=00b27c5156797d33b2bfdd1a283921969f03bcdc661f5b8d6e1ca00afdc588f5",
    "preview": "https://vufoodads.s3.ap-southeast-1.amazonaws.com/vufood/ads/2021/06/04/26081faf15f53c284db7d8489e372c8b.?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJWV2UWBGOU7M53TA%2F20210604%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Date=20210604T092446Z&X-Amz-SignedHeaders=host&X-Amz-Expires=1800&X-Amz-Signature=47e061c61d45a1fd320f6a7451371bb731096726603855527d7f0a94ac999236",
    "type": "video",
    "file_db": "jpg",
    "fileStoreURL": "vufood/ads/2021/06/04/26081faf15f53c284db7d8489e372c8b.jpg",
    "debug": ""
    }
     */
    var fileStoreURL = ""
    var file_db = ""
    var preview = ""
    var type = ""
    var upload = ""
}