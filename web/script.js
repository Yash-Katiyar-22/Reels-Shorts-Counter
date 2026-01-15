document.addEventListener('DOMContentLoaded', () => {
    // Animate stats numbers
    const counters = document.querySelectorAll('.count');

    counters.forEach(counter => {
        const target = +counter.getAttribute('data-target');
        const isFloat = target % 1 !== 0;
        let count = 0;

        const updateCount = () => {
            const speed = 200; // lower is slower
            const inc = target / speed;

            if (count < target) {
                count += inc;

                if (counter.innerText.includes('hours')) {
                    counter.innerText = (isFloat ? count.toFixed(1) : Math.ceil(count)) + ' hours';
                } else {
                    counter.innerText = Math.ceil(count);
                }
                setTimeout(updateCount, 10);
            } else {
                if (counter.innerText.includes('hours')) {
                    counter.innerText = target + ' hours';
                } else {
                    counter.innerText = target;
                }
            }
        };
        updateCount();
    });

    // Smooth scroll
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelector(this.getAttribute('href')).scrollIntoView({
                behavior: 'smooth'
            });
        });
    });
});
