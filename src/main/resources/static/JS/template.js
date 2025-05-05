function confirmDelete(button) {
    var productName = button.getAttribute("data-product-name");
    return confirm("本当に削除しますか？ 商品名: " + productName + " ?");
}

//カートの商品削除
 function deleteCartItem(cartItemId) {
        if (!confirm("この商品をカートから削除しますか？")) return;

        const form = document.createElement("form");
        form.method = "post";
        form.action = "/cart/delete";

        const input = document.createElement("input");
        input.type = "hidden";
        input.name = "cartItemId";
        input.value = cartItemId;

        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();
    }