function confirmDelete(button) {
    var productName = button.getAttribute("data-product-name");
    return confirm("本当に削除しますか？ 商品名: " + productName + " ?");
}