// 家計簿アプリ JavaScript

$(document).ready(function() {
    // アラートの自動非表示（5秒後）
    setTimeout(function() {
        $('.alert:not(.alert-permanent)').fadeOut('slow');
    }, 5000);

    // 削除確認
    $('[data-confirm-delete]').on('click', function(e) {
        if (!confirm('本当に削除しますか？')) {
            e.preventDefault();
        }
    });

    // 金額のカンマ区切り表示
    $('.amount').each(function() {
        var amount = $(this).text();
        $(this).text(Number(amount).toLocaleString());
    });
});
