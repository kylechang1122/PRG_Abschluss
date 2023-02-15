function showCommentEditor($target, data) {
    // schema of Comment Edit for Alpaca
    var schema = {
        title: "Comment Edit",
        type: "object",
        properties: {
            content: {
                type: "string",
                title: "Comment",
            },
        },
    };
    var options = {
        fields: {
        },
        form: {
            buttons: {
                submit: {
                    click: function() {
                        var value = this.getValue();
                        console.log(value)
                    },
                    title: "Save"
                },
                cancel: {
                    click: () => $target.html(''),
                    title: "Cancel"
                }
            }
        }
    };
    $target.alpaca({
        data: data,
        schema: schema,
        options: options
    });
}
