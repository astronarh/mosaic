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

    $('#brightness').change(function () {
        $('#brightness-value').text($('#brightness').val());

        $.post('/updateImage', { brightness : $('#brightness').val() } );

        sleep(500);

        $.post('/getImage', function (data) {
            $('#image').attr('src', 'data:image/jpeg;base64,' + data.pic);
        });

        $.post('/getPixelatedImage', function (data) {
            $('#image_table').html("");
            $('#image_table').append(data);
        });
    });

    //$('#image').attr('src', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==')

    $.post('/getImage', function (data) {
        $('#image').attr('src', 'data:image/jpeg;base64,' + data.pic);
    })
});

function sleep(ms) {
    ms += new Date().getTime();
    while (new Date() < ms){}
}