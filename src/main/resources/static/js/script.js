const BUFFER_SIZE = 512 * 1024;

function splitFile() {
    const file = document.getElementById('inputFile').files[0];
    const username = document.getElementById('username').value;
    const progressBar = document.getElementById('progress-bar');
    const requestURL = `/upload/partial?username=${username}&filename=${file.name}`

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
        } else alert('Файл загружен');
    };

    function readNext() {
        let slice = file.slice(offset, offset += BUFFER_SIZE);
        reader.readAsArrayBuffer(slice);
        counter++;
    }

    readNext();
}
