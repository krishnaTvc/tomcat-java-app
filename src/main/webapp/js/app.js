/**
 * Travel App - Client-side UI & BMI Interactive Logic
 */

document.addEventListener('DOMContentLoaded', () => {
    initBmiCalculator();
    initCountryChips();
    initTableSearch();
    initFormValidation();
});

/**
 * Live BMI Calculator with category badges
 */
function initBmiCalculator() {
    const heightInput = document.getElementById('heightInput');
    const weightInput = document.getElementById('weightInput');
    const bmiInput = document.getElementById('bmi');
    const badge = document.getElementById('bmiBadge');

    function calculateFromHeightWeight() {
        if (!heightInput || !weightInput || !bmiInput) return;

        const hCm = parseFloat(heightInput.value);
        const wKg = parseFloat(weightInput.value);

        if (hCm > 0 && wKg > 0) {
            const hM = hCm / 100.0;
            const bmi = (wKg / (hM * hM)).toFixed(2);
            bmiInput.value = bmi;
            updateBmiBadge(parseFloat(bmi));
        }
    }

    if (heightInput && weightInput) {
        heightInput.addEventListener('input', calculateFromHeightWeight);
        weightInput.addEventListener('input', calculateFromHeightWeight);
    }

    if (bmiInput) {
        bmiInput.addEventListener('input', (e) => {
            const val = parseFloat(e.target.value);
            if (!isNaN(val) && val > 0) {
                updateBmiBadge(val);
            } else if (badge) {
                badge.textContent = 'Enter BMI';
                badge.className = 'bmi-badge-display badge-neutral';
            }
        });

        // Initialize badge if BMI already present (e.g., from server validation redirect)
        if (bmiInput.value) {
            const initialBmi = parseFloat(bmiInput.value);
            if (!isNaN(initialBmi)) {
                updateBmiBadge(initialBmi);
            }
        }
    }

    function updateBmiBadge(val) {
        if (!badge) return;

        let category = '';
        let className = 'bmi-badge-display ';

        if (val < 18.5) {
            category = 'Underweight (' + val + ')';
            className += 'badge-info';
        } else if (val < 25.0) {
            category = 'Normal (' + val + ')';
            className += 'badge-success';
        } else if (val < 30.0) {
            category = 'Overweight (' + val + ')';
            className += 'badge-warning';
        } else {
            category = 'Obese (' + val + ')';
            className += 'badge-danger';
        }

        badge.textContent = category;
        badge.className = className;
    }
}

/**
 * Clickable country recommendation chips
 */
function initCountryChips() {
    const chips = document.querySelectorAll('.chip-btn');
    const countryInput = document.getElementById('desiredCountry');

    if (!countryInput) return;

    chips.forEach(chip => {
        chip.addEventListener('click', (e) => {
            e.preventDefault();
            const countryName = chip.getAttribute('data-country');
            if (countryName) {
                countryInput.value = countryName;
                countryInput.focus();

                // Highlight chip briefly
                chip.style.backgroundColor = 'var(--primary-light)';
                chip.style.borderColor = 'var(--primary)';
                setTimeout(() => {
                    chip.style.backgroundColor = '';
                    chip.style.borderColor = '';
                }, 400);
            }
        });
    });
}

/**
 * Instant search & filtering on customers table
 */
function initTableSearch() {
    const searchInput = document.getElementById('customerSearch');
    const table = document.getElementById('customerTable');

    if (!searchInput || !table) return;

    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.toLowerCase().trim();
        const rows = table.querySelectorAll('tbody tr');

        let visibleCount = 0;
        rows.forEach(row => {
            const text = row.textContent.toLowerCase();
            if (text.includes(query)) {
                row.style.display = '';
                visibleCount++;
            } else {
                row.style.display = 'none';
            }
        });

        const emptyMsg = document.getElementById('searchEmptyMsg');
        if (emptyMsg) {
            emptyMsg.style.display = (visibleCount === 0 && rows.length > 0) ? 'block' : 'none';
        }
    });
}

/**
 * Client-side validation prior to form submission
 */
function initFormValidation() {
    const form = document.getElementById('registrationForm');
    if (!form) return;

    form.addEventListener('submit', (e) => {
        const name = document.getElementById('name').value.trim();
        const age = parseInt(document.getElementById('age').value, 10);
        const gender = document.getElementById('gender').value;
        const bmi = parseFloat(document.getElementById('bmi').value);
        const country = document.getElementById('desiredCountry').value.trim();

        if (!name) {
            alert('Please enter your full name.');
            e.preventDefault();
            return;
        }

        if (isNaN(age) || age < 1 || age > 120) {
            alert('Please enter a valid age between 1 and 120.');
            e.preventDefault();
            return;
        }

        if (!gender) {
            alert('Please select your gender.');
            e.preventDefault();
            return;
        }

        if (isNaN(bmi) || bmi <= 0 || bmi > 100) {
            alert('Please enter a valid BMI (or calculate using height and weight).');
            e.preventDefault();
            return;
        }

        if (!country) {
            alert('Desired Country to Travel is mandatory.');
            e.preventDefault();
            return;
        }
    });
}
