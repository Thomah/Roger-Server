function refresh() {
    var textButton = document.getElementById("refresh");
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/api/files");
    xhr.onload = function () {
      if (xhr.status === 200) {
        textButton.classList.remove('btn-primary');
        textButton.classList.remove('btn-error');
        textButton.classList.add('btn-success');
        var json = JSON.parse(xhr.responseText);
      } else {
        textButton.classList.remove('btn-primary');
        textButton.classList.remove('btn-success');
        textButton.classList.add('btn-error');
      }
    };
    xhr.send();
}
