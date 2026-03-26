import { api } from '@/lib/api'
import type { InitiatePaymentRequest, Transaction } from '../types'

export async function initiateEcoCash(data: InitiatePaymentRequest): Promise<Transaction> {
  const res = await api.post('/payments/ecocash', data)
  return res.data
}

export async function initiateInnBucks(data: InitiatePaymentRequest): Promise<Transaction> {
  const res = await api.post('/payments/innbucks', data)
  return res.data
}

export async function getTransactions(): Promise<Transaction[]> {
  const res = await api.get('/payments/transactions')
  return res.data
}
