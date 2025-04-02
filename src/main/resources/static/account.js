function handleAccountTypeChange() {
    console.log("Account type changed");
    var accountType = document.getElementById("accountType").value;
    var balanceField = document.getElementById("balance");
    if (accountType === "Credit") {
        balanceField.value = 0;
        balanceField.disabled = true;
    } else {
        balanceField.disabled = false;
    }
}