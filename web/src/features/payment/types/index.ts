export type PaymentMethod = 'ECOCASH' | 'INNBUCKS'

export interface InitiatePaymentRequest {
  listingId: string
  method: PaymentMethod
  amount: number
  currency: 'ZIG' | 'USD'
}

export interface Transaction {
  id: string
  listingId: string
  buyerId: string
  sellerId: string
  amount: number
  currency: 'ZIG' | 'USD'
  method: PaymentMethod
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED' | 'REFUNDED'
  gatewayReference: string | null
  createdAt: string
  updatedAt: string
}
