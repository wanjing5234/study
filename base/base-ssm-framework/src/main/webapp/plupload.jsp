<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>

<html>
    <body>

        <h2>Hello World!</h2>

        <div id="uploader">
            <p>Your browser doesn't have Flash, Silverlight or HTML5 support.</p>
        </div>
        <button id="toStop">暂停一下</button>
        <button id="toStart">再次开始</button>

    </body>
</html>

<!-- 配置界面上的css -->
<link rel="stylesheet" type="text/css" href="${path}plupload/js/jquery.plupload.queue/css/jquery.plupload.queue.css">
<script type="text/javascript" src="${path}js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${path}plupload/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${path}plupload/js/jquery.plupload.queue/jquery.plupload.queue.js"></script>

<!-- 国际化中文支持 -->
<script type="text/javascript" src="${path}plupload/js/i18n/zh_CN.js"></script>

<script type="text/javascript">
    $(function() {
        // Initialize the widget when the DOM is ready
        var uploader = $("#uploader").pluploadQueue({
            // General settings
            runtimes: 'html5,flash,silverlight,html4',
            url: "http://localhost:82/plupload/upload",

            // Maximum file size
            max_file_size: '10000mb',

            chunk_size: '1mb',

            // Resize images on clientside if we can
            resize: {
                width: 200,
                height: 200,
                quality: 90,
                crop: true // crop to exact dimensions
            },

            // Specify what files to browse for
            filters: [
                {title: "Image files", extensions: "jpg,gif,png"},
                {title: "Vedio files", extensions: "mp4,mkv"},
                {title: "Zip files", extensions: "zip,avi"}
            ],

            // Rename files by clicking on their titles
            rename: true,

            // Sort files
            sortable: true,

            // Enable ability to drag'n'drop files onto the widget (currently only HTML5 supports that)
            dragdrop: true,

            // Views to activate
            views: {
                list: true,
                thumbs: true, // Show thumbs
                active: 'thumbs'
            },

            // Flash settings
            flash_swf_url: 'js/Moxie.swf',

            // Silverlight settings
            silverlight_xap_url: 'js/Moxie.xap'
        });

        $("#toStop").on('click', function () {
            uploader.stop();
        });

        $("#toStart").on('click', function () {
            uploader.start();
        });
    });
</script>