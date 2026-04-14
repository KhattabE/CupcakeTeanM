function goBack() {
    if (document.referrer !== "") {
        window.history.back();
    } else {
        window.location.href = "/";
    }
}