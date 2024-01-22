const BUFFER_SIZE = 512 * 1024;
let FILE_PATH = "/";

function splitFile() {
    const file = document.getElementById('inputFile').files[0];
    const progressBar = document.getElementById('progress-bar');
    const requestURL = `/api/file/upload/partial?filename=${FILE_PATH}/${file.name}`

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
    const treeTable = document.getElementById('tree-table');

    if (filepath === undefined) filepath = "/";
    const requestURL = `/api/file/tree?filepath=${filepath}`;
    treeTable.innerHTML = '';

    fetch(requestURL, {
        method: 'GET',
    }).then(async (response) => {
        const result = await response.json();
        FILE_PATH = filepath;
        for (let i = 0; i < result.length; i++) {
            let row = treeTable.insertRow();

            let iconCell = row.insertCell(0);
            let hrefCell = row.insertCell(1);
            let smthCell = row.insertCell(2);

            if (result[i].fileType === 'FILE') {
                row.setAttribute('onclick', `location.href='/api/file/download?filename=${result[i].fileName}'`);
            } else {
                row.setAttribute('onclick', `tree('${result[i].filePath}')`);
            }

            iconCell.innerHTML = (result[i].fileType === 'FILE') ? 'file' : 'dir';
            hrefCell.innerHTML = result[i].fileName;
            smthCell.innerHTML = result[i].fileSize + 'B';

            console.log(result[i].filePath);
        }
    });
}

function back() {
    let filePath = "";
    let filePathSplit = FILE_PATH.split("/").slice(1, -1);
    filePathSplit.forEach(part => filePath += `/${part}`);
    tree(filePath);
}
