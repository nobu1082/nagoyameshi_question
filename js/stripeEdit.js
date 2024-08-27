 document.addEventListener('DOMContentLoaded', (event) => {
    document.querySelector('#updateCardLink').addEventListener('click', (e) => {
        e.preventDefault();
        fetch('/stripeEdit')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
             
                    window.location.href = `/edit-cards`;
                } else {
                    alert(data.error);
                }
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation:', error);
                alert('stripeEdit.js サーバーエラーが発生しました');
            });
    });
});