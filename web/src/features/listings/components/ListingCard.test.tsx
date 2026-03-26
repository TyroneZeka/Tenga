import { render, screen } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import { ListingCard } from './ListingCard'
import type { Listing } from '../types'

const listing: Listing = {
  id: '1',
  title: 'iPhone 12 Pro',
  description: 'Great condition',
  price: 350,
  currency: 'USD',
  condition: 'GOOD',
  categoryId: 'electronics',
  categoryName: 'Electronics',
  cityId: 'harare',
  cityName: 'Harare',
  latitude: -17.82,
  longitude: 31.05,
  imageUrls: [],
  seller: { id: 's1', displayName: 'Alice', avatarUrl: null, trustScore: 4.5 },
  status: 'ACTIVE',
  createdAt: '2026-03-01T10:00:00Z',
}

function renderCard(overrides: Partial<Listing> = {}) {
  return render(
    <MemoryRouter>
      <ListingCard listing={{ ...listing, ...overrides }} />
    </MemoryRouter>,
  )
}

it('renders listing title and price', () => {
  renderCard()
  expect(screen.getByText('iPhone 12 Pro')).toBeInTheDocument()
  expect(screen.getByText('US$ 350')).toBeInTheDocument()
})

it('renders ZiG currency correctly', () => {
  renderCard({ currency: 'ZIG', price: 450 })
  expect(screen.getByText('ZiG 450')).toBeInTheDocument()
})

it('renders city name', () => {
  renderCard()
  expect(screen.getByText('Harare')).toBeInTheDocument()
})

it('renders trust score', () => {
  renderCard()
  expect(screen.getByText('4.5')).toBeInTheDocument()
})

it('shows "No photo" placeholder when no images', () => {
  renderCard({ imageUrls: [] })
  expect(screen.getByText('No photo')).toBeInTheDocument()
})

it('links to listing detail page', () => {
  renderCard()
  expect(screen.getByRole('link')).toHaveAttribute('href', '/listings/1')
})
