// src/main/resources/static/js/neostore.js
document.addEventListener('DOMContentLoaded', function () {
  // Countdown demo - if you render server-side values prefer to initialize with those values.
  (function initCountdown() {
    const cd = document.getElementById('countdown');
    if (!cd) return;
    // Fallback to 7 minutes 6 seconds if no server value
    let remaining = (cd.dataset.seconds ? parseInt(cd.dataset.seconds, 10) : (7 * 60 + 6));
    function render() {
      const h = String(Math.floor(remaining / 3600)).padStart(2, '0');
      const m = String(Math.floor((remaining % 3600) / 60)).padStart(2, '0');
      const s = String(remaining % 60).padStart(2, '0');
      cd.innerHTML = `<span class="unit"><b>${h}</b> HOURS</span>
                      <span class="unit"><b>${m}</b> MINUTES</span>
                      <span class="unit"><b>${s}</b> SECONDS</span>`;
    }
    render();
    const t = setInterval(() => {
      if (remaining <= 0) { clearInterval(t); return; }
      remaining--;
      render();
    }, 1000);
  })();

  // Fade-out visual for remove forms (optimistic UI)
  document.querySelectorAll('.remove-form').forEach(form => {
    form.addEventListener('submit', function (e) {
      const row = form.closest('.cart-item');
      if (row) {
        row.style.transition = 'opacity .28s ease, transform .28s ease';
        row.style.opacity = '0.3';
        row.style.transform = 'translateX(-8px)';
      }
      // Let the form submit normally (server redirects back)
    });
  });

  // Optional: AJAX remove (uncomment to use fetch-based removal and client-side update)
  // Note: If you prefer full page reloads and CSRF handling via forms, keep above behavior.
  /*
  document.querySelectorAll('.remove-form').forEach(form => {
    form.addEventListener('submit', function (e) {
      e.preventDefault();
      const action = form.getAttribute('action');
      const csrfInput = form.querySelector('input[name="_csrf"]');
      const csrfToken = csrfInput ? csrfInput.value : null;
      fetch(action, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          ...(csrfToken ? {'X-CSRF-TOKEN': csrfToken} : {})
        },
        body: ''
      }).then(resp => {
        if (resp.redirected) {
          window.location = resp.url;
        } else {
          // remove from DOM
          const row = form.closest('.cart-item');
          if (row) row.remove();
          // Optionally refresh totals via a small endpoint or recalc client-side
        }
      }).catch(err => {
        console.error('Failed to remove cart item', err);
        alert('Could not remove item. Try again.');
      });
    });
  });
  */

  // Small UX: add-to-cart button feedback
  document.querySelectorAll('form[action^="/cart/add"]').forEach(form => {
    form.addEventListener('submit', function () {
      const btn = form.querySelector('button[type="submit"]');
      if (btn) {
        btn.disabled = true;
        const original = btn.innerHTML;
        btn.innerHTML = 'Added';
        setTimeout(() => {
          btn.disabled = false;
          btn.innerHTML = original;
        }, 900);
      }
    });
  });
});
