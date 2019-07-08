function refresh() {
  overload_xhr("GET",
    "/api/files",
    (xhr) => {
      var json = JSON.parse(xhr.responseText);
      doc_refresh(json);
    },
    () => { },
    () => { },
    "",
    document.getElementById("refresh"));
}

function save() {
  overload_xhr(
    "PUT",
    `/api/files`,
    () => { },
    (xhr) => {
      xhr.setRequestHeader("Content-type", "application/json; charset=utf-8");
    },
    () => { },
    JSON.stringify(doc_getValues()),
    document.getElementById("save")
  );
}

function upload() {
  var form = new FormData();
  var file = document.querySelector("#uploadFile").files[0];
  if (file === undefined) {
    alert("No file provided");
  }
  form.append("file", file);
  overload_xhr(
    'POST',
    '/api/files/upload',
    () => {
      document.getElementById("uploadFile").value = "";
      refresh();
    },
    () => { },
    () => { },
    form,
    document.getElementById("upload")
  );
}

var remove = function remove() {
  var textButton = this.firstChild.parentElement;
  var row = textButton.parentElement.parentElement.parentElement;
  var table = row.parentElement;
  overload_xhr(
    "DELETE",
    `/api/files/${row.id}`,
    () => {
      refresh();
    },
    () => { },
    () => { }
  );
};

function doc_refresh(json) {
  var table = document
    .getElementById("files")
    .getElementsByTagName("tbody")[0];
  table.style.display = "none";

  // Delete previous entries
  var rowCount = table.childNodes.length;
  for (var x = rowCount - 1; x >= 0; x--) {
    table.removeChild(table.childNodes[x]);
  }

  // Append new entries
  var rowId, row, newEntry, cell, button, cellSpan;
  for (rowId in json) {
    row = json[rowId];
    newEntry = document.createElement("tr");
    newEntry.id = row.id;

    // FileName
    cell = document.createElement("td");
    cell.classList.add("fileName");
    cell.textContent = row.fileName;
    newEntry.appendChild(cell);

    // Matches
    cell = document.createElement("td");
    cellContent = document.createElement("input");
    cellContent.type = "text";
    cellContent.classList.add("form-control");
    cellContent.classList.add("matches");
    cellContent.value = row.matches;
    cell.appendChild(cellContent);
    newEntry.appendChild(cell);

    // Is Sync
    cell = document.createElement("td");
    cell.classList.add("isSync");
    cell.textContent = row.isSync;
    newEntry.appendChild(cell);

    // Actions
    cell = document.createElement("td");
    cellSpan = document.createElement("span");

    // -- Delete Button
    button = document.createElement("button");
    button.classList.add("btn");
    button.classList.add("btn-secondary");
    button.appendChild(document.createTextNode("Delete"));
    button.onclick = remove;
    cellSpan.appendChild(button);

    cell.appendChild(cellSpan);
    newEntry.appendChild(cell);

    table.appendChild(newEntry);
  }

  table.style.display = "table-row-group";
}

function doc_getValues() {
  var table = document
    .getElementById("files")
    .getElementsByTagName("tbody")[0];

  var row, values = [];
  for (var x = 0; x < table.childNodes.length; x++) {
    row = table.childNodes[x];
    values.push({
      id: row.id,
      fileName: row.getElementsByClassName("fileName")[0].textContent,
      matches: row.getElementsByClassName("matches")[0].value,
      isSync: row.getElementsByClassName("isSync")[0].textContent === "true"
    });
  }

  return values;
}