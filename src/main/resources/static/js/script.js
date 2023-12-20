const BUFFER_SIZE = 512 * 1024;

function splitFile() {
    const file = document.getElementById('inputFile').files[0];
    const username = document.getElementById('username').value;
    const progressBar = document.getElementById('progress-bar');
    const requestURL = `/api/upload/partial?username=${username}&filename=${file.name}`

    let reader = new FileReader();
    let uploadAttempt = 0, counter = 0, offset = 0;

    let chunkCount = Math.ceil(file.size / BUFFER_SIZE);
    console.log(`Buffer size = ${BUFFER_SIZE}, file size = ${file.size}, chunk count = ${chunkCount}`);

    reader.onload = () => {
        if (counter <= chunkCount && uploadAttempt < 3) {
            fetch(requestURL, {
                method: 'POST',
                body: reader.result
            }).then((response) => {
                if (response.status === 202) {
                    let percent = (counter / chunkCount) * 100;
                    progressBar.setAttribute('style', `width: ${percent}%`);
                    readNext();
                } else {
                    alert('Ошибка. Повторите попытку позже');
                    offset -= BUFFER_SIZE;
                    uploadAttempt++;
                    counter--;

                    readNext();
                }
            });
        } else {
            let end = Date.now();
            console.log(`Время выполнения: ${end - start}мс`);
            alert(`Файл загружен. Время выполнения: ${end - start}мс`);
        }
    };

    function readNext() {
        let slice = file.slice(offset, offset += BUFFER_SIZE);
        reader.readAsArrayBuffer(slice);
        counter++;
    }

    let start = Date.now();
    readNext();
}

function tree(filepath) {
    const username = document.getElementById('username').value;
    const treeTable = document.getElementById('tree-table');
    const requestURL = `/api/tree?username=${username}&filepath=${filepath}`;
    treeTable.innerHTML = '';

    fetch(requestURL, {
        method: 'GET',
    }).then(async (response) => {
        const result = await response.json();
        for (let i = 0; i < result.length; i++) {
            let row = treeTable.insertRow();
            row.setAttribute('onclick', `tree('${result[i].fileName}')`);

            let iconCell = row.insertCell(0);
            let hrefCell = row.insertCell(1);
            let smthCell = row.insertCell(2);

            iconCell.innerHTML = (result[i].fileType === 'FILE') ? 'file' : 'dir';
            hrefCell.innerHTML = result[i].fileName;
            smthCell.innerHTML = 'Что-то';

            console.log(result[i].filePath);
        }
    });
}
