$(document).ready(function () {
    function getLockList(ele, param) {
        commonAjaxRequestParams(ele, param, onGetLockListSuccess, null, null, null);
    }

    function onGetLockListSuccess(ele, data) {
        $("#lockList").empty();
        console.log(data);
        if (data != null) {
            data.locks.forEach(function (item) {
                $("#lockList").append(`
                    <tr class="align-middle text-center">
                        <td class="col-auto">
                            <input class="form-check-input checkbox" type="checkbox"/>
                        </td>
                        <td class="col-6 text-start">
                            <span>${item.lockName}</span>
                            <input type="hidden" class="lock-id" value="${item.lockId}">
                        </td>
                        <td class="col-auto">
                            <input disabled type="checkbox" class="form-check-input"
                                   ${item.lockState ? "checked" : ""}>
                        </td>
                        <td class="col-auto">
                            <button class="btn btn-danger" type="button">
                                <i class="fa fa-lock-open"></i>
                                <span class="btn-title">Open Lock</span>
                            </button>
                        </td>
                    </tr>
                `);
            });
        }
    }

    getLockList($('#searchAction'), {lockName: ""});

    $("#searchInput").on("input", function () {
        getLockList($('#searchAction'), {lockName: $(this).val()});
    })
});
