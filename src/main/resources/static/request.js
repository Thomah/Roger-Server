function overload_xhr(method,
  path,
  success_function = function () { },
  before_function = function () { },
  error_function = function () { },
  params_url = '', textButton) {
  var xhr = new XMLHttpRequest();
  xhr.open(method, path);
  before_function(xhr);
  xhr.onload = function () {
    if (xhr.status === 200) {
      if (textButton !== undefined) {
        textButton.classList.remove('btn-primary');
        textButton.classList.remove('btn-danger');
        textButton.classList.add('btn-success');
      }
      success_function(this);
    } else {
      if (textButton !== undefined) {
        textButton.classList.remove('btn-primary');
        textButton.classList.remove('btn-success');
        textButton.classList.add('btn-danger');
      }
      error_function();
    }
  };
  xhr.send(params_url);
}