const BUFFER_SIZE = 512 * 1024;

function sendRequest(URL, requestData) {
    fetch(URL, {
        method: 'POST',
        body: requestData
    }).then((response) => {
        alert("Файл загружен");
    });
}

function splitFile() {
    const file = document.getElementById('inputFile').files[0];
    const username = document.getElementById('username').value;
    const requestURL = `/upload/partial?username=${username}&filename=${file.name}`

    let reader = new FileReader();

    let chunkCount = Math.ceil(file.size / BUFFER_SIZE);
    console.log(`Buffer size = ${BUFFER_SIZE}, file size = ${file.size}, chunk count = ${chunkCount}`);

    const request = new XMLHttpRequest();

    reader.onload = () => {
        console.log(reader.result);
        request.open('POST', requestURL, false);
        request.send(reader.result);
        if (counter < chunkCount) {
            readNext();
        }
    };

    let offset = 0;
    let counter = 0;
    function readNext() {
        let slice = file.slice(offset, offset += BUFFER_SIZE);
        reader.readAsArrayBuffer(slice);
        counter++;
    }

    readNext();
}

    // for (let i = 0; i < ())

    // reader.readAsArrayBuffer(file);
    //
    // reader.onload = (() => {
    //     let buffer = [];
    //     const fileAsByteArray = reader.result;
    //
    //     for (let i = 0; fileAsByteArray.byteLength; i += BUFFER_SIZE) {
    //         const chunk = fileAsByteArray.slice(i, i + BUFFER_SIZE);
    //         buffer.push(chunk);
    //     }
    //
    //     let counter = 0;
    //
    //     while(counter !== buffer.length) {
    //         request.send(buffer[counter]);
    //
    //         if (request.status === 202) continue;
    //         else counter = 0;
    //
    //         counter++;
    //     }
    // });

    // function readNext() {
    //     let slice = file.slice(offset, offset + BUFFER_SIZE);
    //     reader.readAsArrayBuffer(slice);
    // }

