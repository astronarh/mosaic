$(document).ready( function () {
    var table = $('#imagesTable').DataTable({
        "sAjaxSource": "/list",
        "sAjaxDataProp": "",
        "order": [[ 0, "asc" ]],
        "aoColumns": [
            { "mData": "id"},
            { "mData": "name" },
            { "mData": "type" },
            {
                "render": function (data, type, row) {
                    return '<img src="data:' + row["type"] + ';base64,' + row["pic"] + '"  alt=""/>';
                }
            }
        ]
    });
});