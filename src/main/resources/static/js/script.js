function sendRequest(URL, requestData) {
    fetch(URL, {
        method: 'POST',
        body: requestData
    }).then((response) => {
        console.log(response.status)
    });
}

function splitFile() {
    let file = document.getElementById('inputFile').files[0];
    let requestURL = `/upload/partial?username=kidjesus&filename=${file.name}`
    let base64 = "";

    const reader = new FileReader();
    reader.readAsArrayBuffer(file);

    reader.onload = (() => {
        sendRequest(requestURL, reader.result);
    });

    console.log(base64);
}
