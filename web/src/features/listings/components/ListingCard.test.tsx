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
  status: 'ACTIVE',
  sellerId: 's1',
  categoryId: '00000000-0000-0000-0002-000000000001',
  categoryName: 'Electronics',
  city: 'Harare',
  suburb: 'Avondale',
  negotiable: false,
  viewCount: 12,
  imageUrls: [],
  expiresAt: null,
  createdAt: '2026-03-01T10:00:00Z',
  updatedAt: '2026-03-01T10:00:00Z',
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

it('renders city and suburb', () => {
  renderCard()
  expect(screen.getByText('Harare, Avondale')).toBeInTheDocument()
})

it('renders city only when no suburb', () => {
  renderCard({ suburb: null })
  expect(screen.getByText('Harare')).toBeInTheDocument()
})

it('shows "No photo" placeholder when no images', () => {
  renderCard({ imageUrls: [] })
  expect(screen.getByText('No photo')).toBeInTheDocument()
})

it('links to listing detail page', () => {
  renderCard()
  expect(screen.getByRole('link')).toHaveAttribute('href', '/listings/1')
})
