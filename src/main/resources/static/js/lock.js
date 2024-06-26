$(document).ready(function () {
    // Do POST request to the server for the lock data
    function getLockList(ele, param) {
        commonAjaxRequestParams(ele, param, onGetLockListSuccess, null, null, null);
    }

    // Displaying the locks
    function onGetLockListSuccess(ele, data) {
        $("#lockList").empty();
        if (data != null) {
            data.locks.forEach(function (item) {
                $("#lockList").append(`
                    <tr class="lock align-middle text-center">
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
        checkCheckboxes();
    }

    // Get the list of locks on init
    getLockList($('#searchAction'), {lockName: ''});

    // Search event
    $("#searchInput").on("input", function () {
        getLockList($('#searchAction'), {lockName: $(this).val()});
    });

    // Clear button event
    $('#clearBtn').click(function () {
        $('#searchInput').val('');
        getLockList($('#searchAction'), {lockName: $('#searchInput').val()});
    });

    // Checkbox constraint
    checkCheckboxes();
    function checkCheckboxes() {
        let allChecked = $('.checkbox:checked').length === $('.checkbox').length;
        $('#check-all').prop('checked', allChecked);
    }

    $(document).on('click', '#check-all', function () {
        $('.checkbox').prop('checked', $(this).prop('checked'));
    });

    $(document).on('change', '.checkbox',function () {
        checkCheckboxes();
    });

    // Delete event
    $('#deleteBtn').click(function () {
        let lst = [];
        $('.lock').each(function () {
            if ($(this).find('td > .checkbox').prop('checked')) {
                lst.push($(this).find('td > .lock-id').val());
            }
        })
        let param = {
            lockId: lst
        };
        commonAjaxRequestParams($(this), param, onDataChangeSuccess, null, null, null);
    });

    function onDataChangeSuccess() {
        getLockList($('#searchAction'), {lockName: $('#searchInput').val()});
    }

    // Add event
    $('#addBtn').click(function () {
       $('#addModal').modal('show');
    });

    // Add form validation
    $('#addForm').validate({
        rules: {
            addInput: {
                required: true
            }
        }
    });

    $('#doneAddBtn').click(function () {
        if ($('#addForm').valid()) {
            commonAjaxRequestParams($('#addAction'), {lockName: $('#addInput').val()}, onDataChangeSuccess,
                null, null, null);
            $('#addInput').val('');
            $('#addModal').modal('hide');
        } else {
            
        }
    });

});
