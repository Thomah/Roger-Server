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
  alert("Not implemented yet");
}

function upload() {
  var form = new FormData();
  var file = document.querySelector("#uploadFile").files[0];
  if(file === undefined) {
    alert("No file provided");
  }
  form.append("file", file);
  overload_xhr(
    'POST',
    '/api/files/upload',
    (xhr) => {
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
      table.removeChild(row);
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

    // ID
    cell = document.createElement("td");
    cell.textContent = row.id;
    newEntry.appendChild(cell);

    // FileName
    cell = document.createElement("td");
    cell.textContent = row.fileName;
    newEntry.appendChild(cell);

    // Matches
    cell = document.createElement("td");
    cell.textContent = row.matches;
    cellContent = document.createElement("input");
    cellContent.type = "text";
    cellContent.classList.add("form-control");
    cellContent.value = row.matches;
    cell.appendChild(cellContent);
    newEntry.appendChild(cell);

    // Is Sync
    cell = document.createElement("td");
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