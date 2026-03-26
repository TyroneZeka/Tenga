import { useState } from 'react'
import { X, ChevronDown, ChevronUp } from 'lucide-react'
import { CATEGORIES, ZIMBABWE_CITIES, CONDITION_LABELS } from '@/lib/filterOptions'
import type { ListingSearchParams } from '../types'
import type { Currency, Condition } from '@/lib/constants'

interface FilterPanelProps {
  params: ListingSearchParams
  onChange: (updates: Partial<ListingSearchParams>) => void
}

export function FilterPanel({ params, onChange }: FilterPanelProps) {
  const [showMoreCities, setShowMoreCities] = useState(false)

  const INITIAL_CITY_COUNT = 6
  const displayedCities = showMoreCities ? ZIMBABWE_CITIES : ZIMBABWE_CITIES.slice(0, INITIAL_CITY_COUNT)

  function toggleCategory(id: string) {
    onChange({ categoryId: params.categoryId === id ? undefined : id, page: 0 })
  }

  function toggleCondition(c: Condition) {
    onChange({ condition: params.condition === c ? undefined : c, page: 0 })
  }

  function selectCity(city: string) {
    onChange({ city: params.city === city ? undefined : city, page: 0 })
  }

  function selectCurrency(currency: Currency | undefined) {
    onChange({ currency, page: 0 })
  }

  return (
    <aside className="w-full space-y-5">
      {/* Categories */}
      <div>
        <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-gray-500">Category</p>
        <div className="flex flex-wrap gap-1.5">
          {CATEGORIES.map((cat) => (
            <button
              key={cat.id}
              onClick={() => toggleCategory(cat.id)}
              className={`rounded-full px-3 py-1 text-xs font-medium transition-colors ${
                params.categoryId === cat.id
                  ? 'bg-primary-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {cat.name}
            </button>
          ))}
        </div>
      </div>

      {/* City */}
      <div>
        <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-gray-500">City</p>
        <div className="flex flex-wrap gap-1.5">
          {displayedCities.map((city) => (
            <button
              key={city}
              onClick={() => selectCity(city)}
              className={`rounded-full px-3 py-1 text-xs font-medium transition-colors ${
                params.city === city
                  ? 'bg-primary-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {city}
            </button>
          ))}
        </div>
        {ZIMBABWE_CITIES.length > INITIAL_CITY_COUNT && (
          <button
            onClick={() => setShowMoreCities((v) => !v)}
            className="mt-1.5 flex items-center gap-0.5 text-xs text-primary-600 hover:text-primary-700"
          >
            {showMoreCities ? (
              <>
                <ChevronUp className="h-3 w-3" /> Show less
              </>
            ) : (
              <>
                <ChevronDown className="h-3 w-3" /> {ZIMBABWE_CITIES.length - INITIAL_CITY_COUNT} more cities
              </>
            )}
          </button>
        )}
      </div>

      {/* Condition */}
      <div>
        <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-gray-500">Condition</p>
        <div className="flex flex-wrap gap-1.5">
          {(Object.keys(CONDITION_LABELS) as Condition[]).map((cond) => (
            <button
              key={cond}
              onClick={() => toggleCondition(cond)}
              className={`rounded-full px-3 py-1 text-xs font-medium transition-colors ${
                params.condition === cond
                  ? 'bg-primary-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {CONDITION_LABELS[cond]}
            </button>
          ))}
        </div>
      </div>

      {/* Currency */}
      <div>
        <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-gray-500">Currency</p>
        <div className="flex gap-1.5">
          {(['USD', 'ZIG', undefined] as (Currency | undefined)[]).map((cur) => (
            <button
              key={cur ?? 'any'}
              onClick={() => selectCurrency(cur)}
              className={`rounded-full px-3 py-1 text-xs font-medium transition-colors ${
                params.currency === cur
                  ? 'bg-primary-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {cur === 'USD' ? 'USD' : cur === 'ZIG' ? 'ZiG' : 'Any'}
            </button>
          ))}
        </div>
      </div>

      {/* Price Range */}
      <div>
        <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-gray-500">Price Range</p>
        <div className="flex items-center gap-2">
          <input
            type="number"
            min={0}
            placeholder="Min"
            value={params.minPrice ?? ''}
            onChange={(e) =>
              onChange({ minPrice: e.target.value ? Number(e.target.value) : undefined, page: 0 })
            }
            className="w-full rounded-lg border border-gray-200 bg-gray-50 px-3 py-1.5 text-sm focus:border-primary-400 focus:outline-none focus:ring-1 focus:ring-primary-400"
          />
          <span className="text-gray-400">–</span>
          <input
            type="number"
            min={0}
            placeholder="Max"
            value={params.maxPrice ?? ''}
            onChange={(e) =>
              onChange({ maxPrice: e.target.value ? Number(e.target.value) : undefined, page: 0 })
            }
            className="w-full rounded-lg border border-gray-200 bg-gray-50 px-3 py-1.5 text-sm focus:border-primary-400 focus:outline-none focus:ring-1 focus:ring-primary-400"
          />
        </div>
      </div>

      {/* Clear all */}
      {hasActiveFilters(params) && (
        <button
          onClick={() =>
            onChange({
              categoryId: undefined,
              city: undefined,
              condition: undefined,
              currency: undefined,
              minPrice: undefined,
              maxPrice: undefined,
              page: 0,
            })
          }
          className="flex items-center gap-1 text-xs text-red-500 hover:text-red-600"
        >
          <X className="h-3 w-3" />
          Clear all filters
        </button>
      )}
    </aside>
  )
}

function hasActiveFilters(params: ListingSearchParams) {
  return !!(params.categoryId || params.city || params.condition || params.currency || params.minPrice != null || params.maxPrice != null)
}

interface ActiveChipsProps {
  params: ListingSearchParams
  onChange: (updates: Partial<ListingSearchParams>) => void
}

export function ActiveFilterChips({ params, onChange }: ActiveChipsProps) {
  const chips: { label: string; clear: () => void }[] = []

  if (params.categoryId) {
    const cat = CATEGORIES.find((c) => c.id === params.categoryId)
    chips.push({ label: cat?.name ?? 'Category', clear: () => onChange({ categoryId: undefined, page: 0 }) })
  }
  if (params.city) {
    chips.push({ label: params.city, clear: () => onChange({ city: undefined, page: 0 }) })
  }
  if (params.condition) {
    chips.push({
      label: CONDITION_LABELS[params.condition],
      clear: () => onChange({ condition: undefined, page: 0 }),
    })
  }
  if (params.currency) {
    chips.push({
      label: params.currency === 'ZIG' ? 'ZiG' : 'USD',
      clear: () => onChange({ currency: undefined, page: 0 }),
    })
  }
  if (params.minPrice != null) {
    chips.push({ label: `Min ${params.minPrice}`, clear: () => onChange({ minPrice: undefined, page: 0 }) })
  }
  if (params.maxPrice != null) {
    chips.push({ label: `Max ${params.maxPrice}`, clear: () => onChange({ maxPrice: undefined, page: 0 }) })
  }

  if (chips.length === 0) return null

  return (
    <div className="flex flex-wrap gap-1.5">
      {chips.map((chip) => (
        <span
          key={chip.label}
          className="flex items-center gap-1 rounded-full bg-primary-50 px-3 py-1 text-xs font-medium text-primary-700"
        >
          {chip.label}
          <button onClick={chip.clear} className="hover:text-primary-900">
            <X className="h-3 w-3" />
          </button>
        </span>
      ))}
    </div>
  )
}
