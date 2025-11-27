import React, { useState } from 'react';

export default function StarRating({ value, onChange, name }) {
  const [hoverValue, setHoverValue] = useState(null);

  return (
    <div className="flex gap-2">
      {[1, 2, 3, 4, 5].map((star) => (
        <button
          key={star}
          type="button"
          onClick={() => onChange(name, star)}
          onMouseEnter={() => setHoverValue(star)}
          onMouseLeave={() => setHoverValue(null)}
          className="transition-transform hover:scale-110 focus:outline-none"
        >
          <svg
            className={`w-10 h-10 ${
              star <= (hoverValue || value)
                ? 'fill-blue-600'
                : 'fill-gray-300'
            }`}
            viewBox="0 0 24 24"
          >
            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" />
          </svg>
        </button>
      ))}
    </div>
  );
}
