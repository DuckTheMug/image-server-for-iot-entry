const commonAjaxRequest = function($ele, successFunc, errorFunc, failFunc,
                                   endFunc) {
    if (!$ele.attr('data-action')) {
        return;
    }

    let $fromData = $ele.closest('form').serialize();

    let isDisabledBefore = $ele.prop('disabled');

    $ele.prop('disabled', true);

    $.ajax({
        url: $ele.attr('data-action'),
        type: 'post',
        data: $fromData,
        timeout: 10000,
        dataType: 'json'
    }).done(function(data, textStatus, xhr) {
        viewMessageAjax(data);
        if (data && data["successFlag"]) {
            if (successFunc) {
                successFunc(data["resultMap"]);
            }
        } else {
            if (errorFunc) {
                errorFunc(data["errorMessage"], data["resultMap"]);
            }
        }
    }).fail(function(xhr, textStatus, errorThrown) {if (failFunc) {
            failFunc(xhr);
        }
    }).always(function(xhr, textStatus) {
        if (endFunc) {
            endFunc();
        }
        if (isDisabledBefore !== true) {
            $ele.prop('disabled', false);
        }
    })

}

const commonAjaxRequestParams = function($ele, params, successFunc, errorFunc, failFunc,
                                         endFunc) {

    if (!$ele.attr('data-action')) {
        return;
    }

    let isDisabledBefore = $ele.prop('disabled');

    $ele.prop('disabled', true);
    $ele.attr('data-action');
    console.log(params);
    $.ajax({
        url: $ele.attr('data-action'),
        type: 'post',
        data: params,
        timeout: 10000,
        dataType: 'json'
    }).done(function(data, textStatus, xhr) {
        viewMessageAjax(data);
        if (data && data["successFlag"]) {
            if (successFunc) {
                successFunc($ele, data["resultMap"]);
            }
        } else {
            if (errorFunc) {
                errorFunc(data["errorMessage"], data["resultMap"]);
            }
        }
    }).fail(function(xhr, textStatus, errorThrown) {
        if (failFunc) {
            failFunc(xhr);
        }
    }).always(function(xhr, textStatus) {
        if (endFunc) {
            endFunc();
        }
        if (isDisabledBefore !== true) {
            $ele.prop('disabled', false);
        }
    })
}
function viewMessageAjax(data) {
    clearCommonMessage();

    if (data["errorMessage"]) {
        $('#fieldError-msg-error').append(data["errorMessage"]);
        $("#fieldError-msg-error").parent().removeClass('d-none');
    } else {
        $("#fieldError-msg-error").parent().addClass('d-none');
    }

    if (data["successMessage"]) {
        $('#fieldSuccess-msg-success').append(data["successMessage"]);
        $("#fieldSuccess-msg-success").parent().removeClass('d-none');
    } else {
        $("#fieldSuccess-msg-success").parent().addClass('d-none');
    }
}

function clearCommonMessage() {
    $("#fieldError-msg-error").parent().addClass('d-none');
    $("#fieldError-msg-error").text('');
    $("#fieldSuccess-msg-success").parent().addClass('d-none');
    $("#fieldSuccess-msg-success").text('');
}
