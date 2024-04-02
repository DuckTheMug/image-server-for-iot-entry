$(document).ready(function () {
    // Do POST request to the server for the lock data
    function getLockList(ele, param) {
        commonAjaxRequestParams(ele, param, onGetLockListSuccess, null, null, null);
    }

    // Displaying the locks
    function onGetLockListSuccess(ele, data) {
        $("#lockList").empty();
        console.log(data);
        if (data != null) {
            data.locks.forEach(function (item) {
                $("#lockList").append(`
                    <tr class="align-middle text-center">
                        <td class="col-auto">
                            <input class="form-check-input checkbox" type="checkbox"/>
                            <input type="hidden" class="lock-id" value="${item.lockId}">
                        </td>
                        <td class="col-6 text-start">
                            <span>${item.lockName}</span>
                        </td>
                        <td class="col-auto">
                            <input disabled type="checkbox" class="form-check-input"
                                   ${item.lockState ? "checked" : ""}>
                        </td>
                        <td class="col-auto">
                            <button class="btn btn-danger unlock-btn" type="button">
                                <i class="fa fa-lock-open"></i>
                                <span class="btn-title">Open Lock</span>
                            </button>
                        </td>
                    </tr>
                `);
            });
        }
    }

    // Get the list of locks on init
    getLockList($('#searchAction'), {lockName: ""});

    // Search event
    $("#searchInput").on("input", function () {
        getLockList($('#searchAction'), {lockName: $(this).val()});
    })

    // Checkbox constraint
    function checkCheckboxes() {
        let allChecked = $('.checkbox:checked').length === $('.checkbox').length;
        $('#check-all').prop('checked', allChecked);
    }

    $(document).on('click', '#check-all', function() {
        $('.checkbox').prop('checked', $(this).prop('checked'));
    });

    $(document).on('change', '.checkbox',function() {
        checkCheckboxes();
    });

    // Delete event
    $('#deleteBtn').click(function () {
        let param = {
            lockId: $('.checkbox:checked').next('.lock-id').val()
        }
        commonAjaxRequestParams($(this), param, onDeleteSuccess, null, null, null);
    });

    function onDeleteSuccess() {
        getLockList($('#searchAction'), {lockName: $('#searchInput').val()});
    }

});
