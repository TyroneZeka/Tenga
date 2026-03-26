import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Search, Plus, SlidersHorizontal, X } from 'lucide-react'
import { useListings, ListingCard } from '@/features/listings'
import { FilterPanel, ActiveFilterChips } from '@/features/listings/components/FilterPanel'
import type { ListingSearchParams } from '@/features/listings'
import { CATEGORIES } from '@/lib/filterOptions'

export default function FeedPage() {
  const [params, setParams] = useState<ListingSearchParams>({ page: 0, size: 40 })
  const [query, setQuery] = useState('')
  const [drawerOpen, setDrawerOpen] = useState(false)
  const { data, isLoading } = useListings(params)

  function updateParams(updates: Partial<ListingSearchParams>) {
    setParams((p) => ({ ...p, ...updates }))
  }

  function handleSearch(e: React.FormEvent) {
    e.preventDefault()
    updateParams({ query: query.trim() || undefined, page: 0 })
  }

  const hasFilters = !!(
    params.categoryId || params.city || params.condition ||
    params.currency || params.minPrice != null || params.maxPrice != null
  )

  return (
    <div className="min-h-screen bg-[#f5f5f5]">

      {/* ── Header ─────────────────────────────────────────────────── */}
      <header className="sticky top-0 z-20 w-full bg-[#1a1a2e] px-4 py-3">
        <div className="flex items-center gap-3">
          <Link to="/" className="flex-shrink-0 select-none">
            <span className="text-2xl font-black italic tracking-tight text-[#ff6000]">Tenga</span>
          </Link>

          <form onSubmit={handleSearch} className="flex flex-1 overflow-hidden rounded-md border-2 border-[#ff6000] bg-white">
            <input
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="min-w-0 flex-1 bg-transparent px-4 py-2 text-sm text-gray-800 placeholder:text-gray-400 focus:outline-none"
              placeholder="Search Tenga…"
            />
            <button
              type="submit"
              className="flex items-center gap-1.5 bg-[#ff6000] px-5 py-2 text-sm font-bold text-white hover:bg-[#e55500]"
            >
              <Search className="h-4 w-4" />
              Search
            </button>
          </form>

          <Link
            to="/listings/new"
            className="flex flex-shrink-0 items-center gap-1.5 rounded-md bg-[#ff6000] px-4 py-2 text-sm font-bold text-white hover:bg-[#e55500]"
          >
            <Plus className="h-4 w-4" />
            Sell
          </Link>
        </div>
      </header>

      {/* ── Category strip ────────────────────────────────────────── */}
      <div className="sticky top-[57px] z-10 w-full border-b-2 border-gray-200 bg-white">
        <div className="flex w-full overflow-x-auto" style={{ scrollbarWidth: 'none' }}>
          {/* All */}
          <button
            onClick={() => updateParams({ categoryId: undefined, page: 0 })}
            className={`flex flex-1 flex-shrink-0 flex-col items-center gap-1 px-4 py-3 text-center transition-colors ${
              !params.categoryId
                ? 'border-b-[3px] border-[#ff6000] text-[#ff6000]'
                : 'border-b-[3px] border-transparent text-gray-600 hover:text-[#ff6000]'
            }`}
          >
            <span className="text-2xl leading-none">🛒</span>
            <span className="whitespace-nowrap text-xs font-semibold">All</span>
          </button>

          {CATEGORIES.map((cat) => (
            <button
              key={cat.id}
              onClick={() =>
                updateParams({ categoryId: params.categoryId === cat.id ? undefined : cat.id, page: 0 })
              }
              className={`flex flex-1 flex-shrink-0 flex-col items-center gap-1 px-4 py-3 text-center transition-colors ${
                params.categoryId === cat.id
                  ? 'border-b-[3px] border-[#ff6000] text-[#ff6000]'
                  : 'border-b-[3px] border-transparent text-gray-600 hover:text-[#ff6000]'
              }`}
            >
              <span className="text-2xl leading-none">{cat.icon}</span>
              <span className="whitespace-nowrap text-xs font-semibold">{cat.name.split(' ')[0]}</span>
            </button>
          ))}
        </div>
      </div>

      {/* ── Filter bar ────────────────────────────────────────────── */}
      <div className="w-full border-b border-gray-200 bg-white px-4 py-2">
        <div className="flex items-center justify-between gap-2">
          <div className="flex min-w-0 flex-1 flex-wrap items-center gap-1.5">
            {params.query && (
              <span className="flex items-center gap-1 rounded-full bg-orange-50 px-3 py-0.5 text-xs font-medium text-[#ff6000]">
                "{params.query}"
                <button onClick={() => { setQuery(''); updateParams({ query: undefined, page: 0 }) }}>
                  <X className="h-3 w-3" />
                </button>
              </span>
            )}
            <ActiveFilterChips params={params} onChange={updateParams} />
            {!hasFilters && !params.query && data && (
              <span className="text-xs text-gray-400">{data.totalElements.toLocaleString()} listings</span>
            )}
          </div>

          <button
            onClick={() => setDrawerOpen(true)}
            className={`flex flex-shrink-0 items-center gap-1.5 rounded-md border px-3 py-1.5 text-xs font-semibold transition ${
              hasFilters
                ? 'border-[#ff6000] text-[#ff6000]'
                : 'border-gray-300 text-gray-600 hover:border-[#ff6000] hover:text-[#ff6000]'
            }`}
          >
            <SlidersHorizontal className="h-3.5 w-3.5" />
            Filters
            {hasFilters && (
              <span className="flex h-4 w-4 items-center justify-center rounded-full bg-[#ff6000] text-[9px] font-bold text-white">
                !
              </span>
            )}
          </button>
        </div>
      </div>

      {/* ── Product grid ──────────────────────────────────────────── */}
      <main className="w-full px-3 py-3">
        {isLoading && (
          <div className="grid grid-cols-2 gap-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 2xl:grid-cols-8">
            {Array.from({ length: 16 }).map((_, i) => (
              <div key={i} className="aspect-[3/4] animate-pulse rounded-md bg-gray-200" />
            ))}
          </div>
        )}

        {!isLoading && data && (
          <>
            {data.content.length === 0 ? (
              <div className="py-24 text-center">
                <p className="text-gray-500">No listings found.</p>
                {(hasFilters || params.query) && (
                  <button
                    onClick={() => { setQuery(''); setParams({ page: 0, size: 40 }) }}
                    className="mt-3 text-sm text-[#ff6000] hover:underline"
                  >
                    Clear all filters
                  </button>
                )}
              </div>
            ) : (
              <div className="grid grid-cols-2 gap-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 2xl:grid-cols-8">
                {data.content.map((listing) => (
                  <ListingCard key={listing.id} listing={listing} />
                ))}
              </div>
            )}

            {data.totalPages > 1 && (
              <div className="mt-8 flex items-center justify-center gap-3">
                <button
                  disabled={data.first}
                  onClick={() => updateParams({ page: (params.page ?? 0) - 1 })}
                  className="rounded-md border border-gray-300 bg-white px-6 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 disabled:opacity-40"
                >
                  Prev
                </button>
                <span className="text-sm text-gray-500">
                  {data.number + 1} / {data.totalPages}
                </span>
                <button
                  disabled={data.last}
                  onClick={() => updateParams({ page: (params.page ?? 0) + 1 })}
                  className="rounded-md border border-gray-300 bg-white px-6 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 disabled:opacity-40"
                >
                  Next
                </button>
              </div>
            )}
          </>
        )}
      </main>

      {/* ── Filter drawer ─────────────────────────────────────────── */}
      {drawerOpen && (
        <div className="fixed inset-0 z-30 flex">
          <div className="absolute inset-0 bg-black/40" onClick={() => setDrawerOpen(false)} />
          <div className="relative ml-auto flex h-full w-80 flex-col bg-white shadow-xl">
            <div className="flex items-center justify-between border-b border-gray-200 px-4 py-3">
              <h2 className="font-semibold text-gray-900">Filters</h2>
              <button onClick={() => setDrawerOpen(false)} className="text-gray-400 hover:text-gray-700">
                <X className="h-5 w-5" />
              </button>
            </div>
            <div className="flex-1 overflow-y-auto p-4">
              <FilterPanel
                params={params}
                onChange={(updates) => { updateParams(updates); setDrawerOpen(false) }}
              />
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
